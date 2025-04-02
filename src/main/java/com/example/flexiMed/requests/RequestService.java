package com.example.flexiMed.requests;

import com.example.flexiMed.ambulances.AmbulanceDTO;
import com.example.flexiMed.ambulances.AmbulanceEntity;
import com.example.flexiMed.ambulances.AmbulanceRepository;
import com.example.flexiMed.ambulances.AmbulanceService;
import com.example.flexiMed.notifications.email.EmailService;
import com.example.flexiMed.notifications.notification.NotificationController;
import com.example.flexiMed.notifications.sms.SMSService;
import com.example.flexiMed.patients.PatientRecordsDTO;
import com.example.flexiMed.patients.PatientRecordsService;
import com.example.flexiMed.servicesHistory.ServiceHistoryDTO;
import com.example.flexiMed.servicesHistory.ServiceHistoryService;
import com.example.flexiMed.users.*;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class RequestService {
    private final RequestRepository requestRepository;
    private final AmbulanceRepository ambulanceRepository;
    private final AmbulanceService ambulanceService;
    private final UserRepository userRepository;
    private final PatientRecordsService patientRecordsService;
    private final EmailService emailService;
    private final SMSService smsService;
    private final NotificationController notificationController;
    private final ServiceHistoryService serviceHistoryService;
    private final UserService userService;


    public RequestService(RequestRepository requestRepository, AmbulanceRepository ambulanceRepository,
                          UserRepository userRepository,
                          PatientRecordsService patientRecordsService,
                          EmailService emailService,
                          SMSService smsService,
                          NotificationController notificationController,
                          AmbulanceService ambulanceService,
                          ServiceHistoryService serviceHistoryService,
                          UserService userService
                          ) {
        this.requestRepository = requestRepository;
        this.ambulanceRepository = ambulanceRepository;
        this.ambulanceService = ambulanceService;
        this.userRepository = userRepository;
        this.patientRecordsService = patientRecordsService;
        this.emailService = emailService;
        this.smsService = smsService;
        this.notificationController = notificationController;
        this.serviceHistoryService = serviceHistoryService;
        this.userService = userService;

    }

    public RequestDTO createRequest(RequestDTO request) {
        UserEntity user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Retrieve the first available ambulance
        AmbulanceEntity ambulance = ambulanceRepository.findFirstByavailabilityStatusIsTrue()
                .orElseThrow(() -> new RuntimeException("No available ambulance at the moment"));

        // Create request entity with the selected ambulance
        RequestEntity requestEntity = RequestMapper.toEntity(request, user, ambulance);

        // Save request and update ambulance availability
        RequestEntity savedRequest = requestRepository.save(requestEntity);

        // Save patient record
        String userContact;
        if(user.getPhoneNumber() == null){
            userContact = user.getEmail();
        }else {
            userContact = user.getPhoneNumber();
        }
        System.out.println("your patient record" + savedRequest.getId() +" " + user.getUserId() + " " + userContact + " " + savedRequest.getDescription());
        PatientRecordsDTO patientRecord = new PatientRecordsDTO(
                null,
                savedRequest.getId(),
                user.getUserId(),
                userContact,
                savedRequest.getDescription()
        );
        patientRecordsService.addPatientRecord(patientRecord);


        long etaInMinutes = calculateETA(ambulance.getLatitude(), ambulance.getLongitude(), request.getLatitude(),
                request.getLongitude());



        //Send notifications to user
//        sendEmail(user.getEmail(), user.getName(), etaInMinutes +"minutes", ambulance.getPlateNumber());
//        smsService.sendSms(user.getPhoneNumber(),  "An ambulance has been dispatched to your location\n ETA: "
//                + etaInMinutes);

        // Send real-time notification
        sendNotifications(user, ambulance,  formatTime(etaInMinutes), savedRequest);
//        sendNotifications(getDispatcher(ambulance.getDriverContact()), ambulance,  etaInMinutes, savedRequest);

        //Record service history
        recordServiceHistory(savedRequest);


        return dispatchAmbulance(savedRequest.getId(), savedRequest.getAmbulance().getId());
    }

    public List<RequestDTO> getAllRequests() {
        List<RequestEntity> requests = requestRepository.findAll();
        return requests.stream().map(RequestMapper::toDTO).toList();
    }

    public void recordServiceHistory(RequestEntity request){
        ServiceHistoryDTO  service = new ServiceHistoryDTO(null,request.getId(),
                request.getRequestStatus().toString(),
                request.getRequestTime(),
                request.getDescription());

        serviceHistoryService.logEvent(service);
    }


    public List<RequestDTO> getUserRequests(UUID userId) {
        List<RequestEntity> requests = requestRepository.findByUserUserId(userId);
        if (requests.isEmpty()) {
            throw new EntityNotFoundException("No requests found for user with ID: " + userId);
        }
        return requests.stream().map(RequestMapper::toDTO).toList();
    }

    @Transactional
    public RequestDTO dispatchAmbulance(UUID requestId, UUID ambulanceId) {
        RequestEntity request = requestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));
        AmbulanceDTO ambulanceDTO = ambulanceService.findClosestAmbulance(request.getLatitude(), request.getLongitude());
        AmbulanceEntity ambulance = ambulanceRepository.findById(ambulanceDTO.getId()).orElseThrow(()->
                new IllegalArgumentException(("Ambulance not found")));

        // Assign ambulance and update status
        request.setAmbulance(ambulance);
        request.setRequestStatus(RequestStatus.DISPATCHED);
        request.setDispatchTime(LocalDateTime.now());

        // Update ambulance availability
        ambulance.setAvailabilityStatus(false);
        ambulanceRepository.save(ambulance);

        // Calculate ETA
        double userLatitude = request.getLatitude();
        double userLongitude = request.getLongitude();
        double ambulanceLatitude = ambulance.getLatitude();
        double ambulanceLongitude = ambulance.getLongitude();

        long etaInMinutes = calculateETA(ambulanceLatitude, ambulanceLongitude, userLatitude, userLongitude);

        // Save estimated arrival time
        request.setArrivalTime(LocalDateTime.now().plusMinutes(etaInMinutes));
        requestRepository.save(request);

        // Convert to DTO and return
        RequestDTO responseDTO = RequestMapper.toDTO(request);
        responseDTO.setArrivalTime(request.getArrivalTime()); // Include ETA in response
        return responseDTO;
    }

    public UserEntity getDispatcher(String driverContact) {
        return userService.getUserByPhoneNumber(driverContact)
                .map(UserMapper::toEntity)
                .orElseThrow(() -> new EntityNotFoundException("Dispatcher not found for phone: " + driverContact));
    }



    public long calculateETA(double lat1, double lon1, double lat2, double lon2) {
        final int EARTH_RADIUS = 6371; // Radius of the Earth in km

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = EARTH_RADIUS * c; // Distance in km

        // Assuming average speed of ambulance is 60 km/h
        double averageSpeed = 60; // km/h
        double timeInHours = distance / averageSpeed; // Time in hours
        return (long) (timeInHours * 60); // Convert to minutes
    }


    public void sendEmail(String userEmail, String userName, String eta, String ambulanceNumber) {
        Context context = new Context();
        context.setVariable("userName", userName);
        context.setVariable("eta", eta);
        context.setVariable("ambulanceNumber", ambulanceNumber);

        try {
            emailService.sendEmail(userEmail, "🚑 Ambulance Dispatched", "ambulance-dispatch", context);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void sendNotifications (UserEntity user, AmbulanceEntity ambulance, String etaInMinutes, RequestEntity request){
        NotificationController.AmbulanceNotificationDTO notificationDTO = new NotificationController.AmbulanceNotificationDTO();
        notificationDTO.setUserId(user.getUserId());
        notificationDTO.setAmbulanceId(ambulance.getId());
        notificationDTO.setMessage("Ambulance has been dispatched to your location");
        notificationDTO.setAmbulancePlateNumber(ambulance.getPlateNumber());
        notificationDTO.setDriverName(ambulance.getDriverName());
        notificationDTO.setDriverContact(ambulance.getDriverContact());
        notificationDTO.setEta(etaInMinutes);

        notificationController.sendAmbulanceDispatchedNotification(user.getUserId(), notificationDTO);
//        if(user.getRole() == Role.DISPATCHER){
//            notificationController.sendAmbulanceDispatchedNotification(
//                    user.getUserId(),
//                    "Your ambulance  has been dispatched to the following location!\n" +
//                            "Longitude: " + request.getLongitude() + "\n" +
//                            "Latitude: " + request.getLatitude() + "\n" +
//                            "Patient name: " + request.getUser().getName()+ "\n" +
//                            "Patient contact: " + request.getUser().getPhoneNumber()+ "\n" +
//                            "Estimated Arrival Time: " + etaInMinutes + " minutes"
//            );
//
//        }else {
//            notificationController.sendAmbulanceDispatchedNotification(
//                    user.getUserId(),
//                    "Ambulance with the following details has been dispatched to your location!\n" +
//                            "Driver Name: " + ambulance.getDriverName() + "\n" +
//                            "Driver Contact: " + ambulance.getDriverContact() + "\n" +
//                            "Ambulance Plate Number: " + ambulance.getPlateNumber() + "\n" +
//                            "Estimated Arrival Time: " + etaInMinutes + " minutes"
//            );
//        }
    }

    public  String formatTime(long minutes) {
        if (minutes < 0) {
            throw new IllegalArgumentException("Minutes cannot be negative.");
        }

        long hours = minutes / 60;
        long remainingMinutes = minutes % 60;
        long seconds = minutes * 60;

        if (hours > 0) {
            return String.format("%d hour(s) and %d minute(s)", hours, remainingMinutes);
        } else if (minutes > 0){
            return String.format("%d minute(s)", minutes);
        } else {
            return String.format("%d second(s)", seconds);
        }
    }

//    public void sendNotifications (UserEntity user, AmbulanceEntity ambulance, Long etaInMinutes ){
//
//            notificationController.sendAmbulanceDispatchedNotification(
//                    user.getUserId(),
//                    "Ambulance with the following details has been dispatched to your location!\n" +
//                            "Driver Name: " + ambulance.getDriverName() + "\n" +
//                            "Driver Contact: " + ambulance.getDriverContact() + "\n" +
//                            "Ambulance Plate Number: " + ambulance.getPlateNumber() + "\n" +
//                            "Estimated Arrival Time: " + etaInMinutes + " minutes"
//            );
//    }
}
