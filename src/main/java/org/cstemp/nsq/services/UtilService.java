/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cstemp.nsq.services;


import lombok.extern.slf4j.Slf4j;
import org.cstemp.nsq.repos.NotificationRepository;
import org.cstemp.nsq.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 *
 * @author chibuezeharry & MarcusDashe
 */
@Service
@Slf4j
public class UtilService {

    @Value("${africa.elimi.api.baseUrl}")
    private String ENV_URL;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;
//
//    public ResponseEntity<?> getStandards() {
//
//        List<Standard> standards = standardRepository.findAll();
//
//        return ResponseEntity.ok(new BaseResponse(true, null, standards));
//    }
//
//    public ResponseEntity<?> addStandard(Standard standardRequest) {
//
//        Standard standard = standardRepository.insert(standardRequest);
//
//        return ResponseEntity.ok(new BaseResponse(true, null, standard));
//    }
//
//    public ResponseEntity<?> updateStandard(String standardId, Standard standardRequest) {
//
//        Standard standard = standardRepository.findById(standardId).orElse(null);
//
//        if (standard == null) {
//            return ResponseEntity.notFound().build();
//        }
//
//        BeanUtils.copyProperties(standardRequest, standard);
//
//        Standard savedStandard = standardRepository.save(standard);
//
//        return ResponseEntity.ok(new BaseResponse(true, null, savedStandard));
//    }
//
//    public ResponseEntity<?> deleteStandard(String standardId) {
//
//        standardRepository.deleteById(standardId);
//
//        return ResponseEntity.ok(new BaseResponse(true, "Deleted Successfully", null));
//    }
//
//    public ResponseEntity<?> getNotification(String notificationId) {
//
//        Notification notification = notificationRepository.findById(notificationId).orElse(null);
//
//        User sender = userRepository.findById(notification.getInitiatorId()).orElse(null);
//
//        User receiver = userRepository.findById(notification.getUserId()).orElse(null);
//
//        if (sender == null && receiver == null) {
//            return ResponseEntity.badRequest().build();
//        }
//
//        notification.setUpdatedAt(Instant.now());
//        notification.setRead(true);
//
//        receiver.getNotifications().remove(notificationId);
//
//        userRepository.save(receiver);
//
//        NotificationResponse notificationResponse = new NotificationResponse();
//
//        BeanUtils.copyProperties(notification, notificationResponse);
//
//        notificationResponse.setSenderName(sender.getFirstName() + " " + sender.getLastName());
//        notificationResponse.setReceiverName(receiver.getFirstName() + " " + receiver.getLastName());
//
//        notificationRepository.save(notification);
//
//        return ResponseEntity.ok(new BaseResponse(true, null, notificationResponse));
//    }
//
//    public ResponseEntity<?> acceptJobNotification(String notificationId) {
//
//        Notification notification = notificationRepository.findById(notificationId).orElse(null);
//
//        User sender = userRepository.findById(notification.getInitiatorId()).orElse(null);
//
//        User receiver = userRepository.findById(notification.getUserId()).orElse(null);
//
//        if (sender == null && receiver == null && notification.getType().equals("Assessment Job Notification")) {
//            return ResponseEntity.badRequest().build();
//        }
//
//        if (notification.getAcceptanceDeadline().isBefore(LocalDate.now())) {
//            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(new BaseResponse(false, null, false));
//        }
//
//        notification.setAccepted(Boolean.TRUE);
//
//        sender.getNotifications().add(notificationId);
//
//        notificationRepository.save(notification);
//
//        userRepository.save(sender);
//
//        return ResponseEntity.ok(new BaseResponse(true, null, true));
//    }
//
//    public ResponseEntity<?> getNotifications(UserDetails userDetails) {
//
//        List<Notification> notifications = notificationRepository.findByUserId(userDetails.getId());
//
//        return ResponseEntity.ok(new BaseResponse(true, null, notifications));
//    }
//
//    public ResponseEntity<?> getNotificationsSent(UserDetails userDetails) {
//
//        List<Notification> notifications = notificationRepository.findByInitiatorId(userDetails.getId());
//
//        return ResponseEntity.ok(new BaseResponse(true, null, notifications));
//    }
//
//    public ResponseEntity<?> notify(Notification notification, UserDetails userDetails) {
//
//        if (notification.getUserId() == null) {
//            return ResponseEntity.badRequest().build();
//        }
//
//        notification.setInitiatorId(userDetails.getId());
//
//        System.out.println(notification.getUserId());
//
//        Notification savedNotification = notificationRepository.insert(notification);
//
//        User user = userRepository.findById(notification.getUserId()).orElse(null);
//
//        user.getNotifications().add(savedNotification.getId());
//
//        userRepository.save(user);
//
//        return ResponseEntity.ok(new BaseResponse(true, null, savedNotification));
//    }
//
//    public ResponseEntity<?> deleteNotification(String notificationId, UserDetails userDetails) {
//
//        notificationRepository.deleteById(notificationId);
//
//        return ResponseEntity.ok(new BaseResponse(true, "Deleted Successfully", null));
//    }
//
//    public ResponseEntity<?> getCentres() throws IOException {
//        String URL = ENV_URL + "centres";
//        CloseableHttpClient httpclient = HttpClientBuilder.create().build();
//        HttpGet httpGet = new HttpGet(URL);
//        CloseableHttpResponse response = httpclient.execute(httpGet);
//
//        HttpEntity entity = response.getEntity();
//        String result = EntityUtils.toString(entity);
//
//        JsonNode jn = new ObjectMapper().readTree(result);
//
//        return ResponseEntity.ok(jn);
//    }
//
//    public ResponseEntity<?> getProgrammes() throws IOException {
//        String URL = ENV_URL + "programmes";
//        CloseableHttpClient httpclient = HttpClientBuilder.create().build();
//        HttpGet httpGet = new HttpGet(URL);
//        CloseableHttpResponse response = httpclient.execute(httpGet);
//
//        HttpEntity entity = response.getEntity();
//        String result = Enti777tyUtils.toString(entity);
//
//        JsonNode jn = new ObjectMapper().readTree(result);
//
//        return ResponseEntity.ok(jn);
//    }

}
