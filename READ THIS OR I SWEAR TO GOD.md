# Secure-Messenger-App

In order to receive messages with this app, you need to register an account. Normally, this can't be done without an admin's intervention.
Here's how to hack the secure messenger app and register an account without an admin's permission:

First log in as an existing user. You can use "James" as the username and "password" as the password.
When you successfully log in to the app, a message will be written to the debug log which looks like this:
"D/PushManager: Set push enabled for endpoint arn: [your ARN]"
To see this message you must use the Android Monitor feature in Android Studio. Copy your ARN because you'll need it later. The ARN should all be one string with no white space.

Next, close the app and look at the class LoginActivity. It should be under app/src/main/java/seproject2/secure_messenger. Find the commented code that looks like this:

            user = new AccountsDO();
            user.setDeviceID("unimportant");
            user.setPassword(md5(mPassword));
            user.setRights(true);
            user.setUsername(mEmail);
            try{
                mapper.save(user);
            } catch (AmazonServiceException aws){
                return false;
            } catch (AmazonClientException ace) {
                return false;
            }
            
Uncomment it and plug the ARN you grabbed earlier as the parameter to user.setDeviceID instead of "unimportant". This is the only thing you need to change. 

Next, compile and run the app again. This time at the login screen, type the username and password you want to use for yourself, then hit the sign in button. It will probably tell you the password is incorrect the first time, but try it again and it should let you in.

And that's it; you should now have an account in the database that you can use to log in and that the messaging service can use to find your device. At this point you can comment the code above back out and recompile and rerun or just proceed as is. 

If you're having issues or just don't want to do all this you can email me at jnbunting9265@gmail.com with your ARN and I can set up an account for you manually. 
