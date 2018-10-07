package herman.friendsmanagement.controller;

import herman.friendsmanagement.model.request.EmailRequest;
import herman.friendsmanagement.model.request.RequestorTargetRequest;
import herman.friendsmanagement.model.request.SenderTextRequest;
import herman.friendsmanagement.model.response.ApiResponse;
import herman.friendsmanagement.model.request.FriendsRequest;
import herman.friendsmanagement.model.User;
import herman.friendsmanagement.service.IUserConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")
@RequestMapping("/api/userConnection")
@RestController
public class UserConnectionController {
    @Autowired
    private IUserConnectionService userConnectionService;

    @PostMapping(path="/create")
    public ApiResponse createConnection(@RequestBody FriendsRequest request) {
        ApiResponse response = new ApiResponse();

        List<String> friends = request.getFriends();
        if (friends.size() != 2) {
            response.setSuccess(false);
            response.setFailedReason("Request parameter friends size is not 2");
            return response;
        }

        String userEmail = friends.get(0);
        String friendEmail = friends.get(1);

        //TODO parse both user email is valid

        try {
            boolean result = userConnectionService.createConnection(userEmail, friendEmail);
            response.setSuccess(result);
            return response;
        } catch (Exception err) {
            response.setSuccess(false);
            response.setFailedReason(err.getMessage());
            return response;
        }
    }

    @PostMapping(path="/friends")
    public ApiResponse listFriends(@RequestBody EmailRequest request) {
        ApiResponse response = new ApiResponse();

        String email = request.getEmail();

        //TODO parse email is valid

        try {
            List<String> emails = new ArrayList<String>();
            List<User> users = userConnectionService.getFriendsByEmail(email);
            for (User user : users) {
                emails.add(user.getEmail());
            }

            response.setSuccess(true);
            response.setFriends(emails);
            response.setCount(emails.size());
            return response;
        } catch (Exception err) {
            response.setSuccess(false);
            response.setFailedReason(err.getMessage());
            return response;
        }
    }

    @PostMapping(path="/commonFriends")
    public ApiResponse findCommonFriends(@RequestBody FriendsRequest request) {
        ApiResponse response = new ApiResponse();

        List<String> friends = request.getFriends();
        if (friends.size() != 2) {
            response.setSuccess(false);
            response.setFailedReason("Request parameter friends size is not 2");
            return response;
        }

        String userEmail1 = friends.get(0);
        String userEmail2 = friends.get(1);

        //TODO parse both user email is valid

        try {
            List<String> emails = new ArrayList<String>();
            List<User> users = userConnectionService.getCommonFriends(userEmail1, userEmail2);
            for (User user : users) {
                emails.add(user.getEmail());
            }

            response.setSuccess(true);
            response.setFriends(emails);
            response.setCount(emails.size());
            return response;
        } catch (Exception err) {
            response.setSuccess(false);
            response.setFailedReason(err.getMessage());
            return response;
        }
    }

    @PostMapping(path="/subscribeUpdates")
    public ApiResponse subscribeUpdates(@RequestBody RequestorTargetRequest request) {
        ApiResponse response = new ApiResponse();

        String requestorEmail = request.getRequestor();
        String targetEmail = request.getTarget();

        //TODO parse both email is valid

        try {
            boolean result = userConnectionService.subscribeUpdates(requestorEmail, targetEmail);
            response.setSuccess(result);
            return response;
        } catch (Exception err) {
            response.setSuccess(false);
            response.setFailedReason(err.getMessage());
            return response;
        }
    }

    @PostMapping(path="/blockUpdates")
    public ApiResponse blockUpdates(@RequestBody RequestorTargetRequest request) {
        ApiResponse response = new ApiResponse();

        String requestorEmail = request.getRequestor();
        String targetEmail = request.getTarget();

        //TODO parse both email is valid

        try {
            boolean result = userConnectionService.blockUpdates(requestorEmail, targetEmail);
            response.setSuccess(result);
            return response;
        } catch (Exception err) {
            response.setSuccess(false);
            response.setFailedReason(err.getMessage());
            return response;
        }
    }

    @PostMapping(path="/listCanReceiveUpdates")
    public ApiResponse listCanReceiveUpdates(@RequestBody SenderTextRequest request) {
        ApiResponse response = new ApiResponse();

        String senderEmail = request.getSender();
        String text = request.getText();

        //TODO parse both email is valid

        try {
            List<User> users = userConnectionService.retrieveEmailCanReceiveUpdates(senderEmail, text);
            List<String> emails = new ArrayList<String>();
            for (User user : users) {
                emails.add(user.getEmail());
            }

            response.setSuccess(true);
            response.setRecipients(emails);
            return response;
        } catch (Exception err) {
            response.setSuccess(false);
            response.setFailedReason(err.getMessage());
            return response;
        }
    }
}
