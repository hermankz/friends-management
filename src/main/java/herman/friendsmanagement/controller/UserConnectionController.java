package herman.friendsmanagement.controller;

import herman.friendsmanagement.model.ApiResponse;
import herman.friendsmanagement.model.FriendsRequest;
import herman.friendsmanagement.model.User;
import herman.friendsmanagement.service.IUserConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")
@RequestMapping("/api")
@RestController
public class UserConnectionController {
    @Autowired
    private IUserConnectionService userConnectionService;

    @PostMapping(path="/userConnection/create")
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

    @PostMapping(path="/userConnection/friends")
    public ApiResponse listFriends(@RequestBody String email) {
        ApiResponse response = new ApiResponse();

        //TODO parse email is valid

        try {
            List<String> emails = new ArrayList<String>();
            List<User> users = userConnectionService.getFriendsByEmail(email);
            for (User user : users) {
                emails.add(user.getEmail());
            }

            response.setSuccess(true);
            response.setFriends(emails);
            return response;
        } catch (Exception err) {
            response.setSuccess(false);
            response.setFailedReason(err.getMessage());
            return response;
        }
    }

    @PostMapping(path="/userConnection/commonFriends")
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
            return response;
        } catch (Exception err) {
            response.setSuccess(false);
            response.setFailedReason(err.getMessage());
            return response;
        }
    }

    @PostMapping(path="/userConnection/subscribeUpdates")
    public ApiResponse subscribeUpdates(@RequestBody String requestorEmail, @RequestBody String targetEmail) {
        ApiResponse response = new ApiResponse();

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

    @PostMapping(path="/userConnection/blockUpdates")
    public ApiResponse blockUpdates(@RequestBody String requestorEmail, @RequestBody String targetEmail) {
        ApiResponse response = new ApiResponse();

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

    @PostMapping(path="/userConnection/listCanReceiveUpdates")
    public ApiResponse listCanReceiveUpdates(@RequestBody String senderEmail, @RequestBody String text) {
        ApiResponse response = new ApiResponse();

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
