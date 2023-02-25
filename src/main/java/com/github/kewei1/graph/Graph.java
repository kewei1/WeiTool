// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT license.

// <ImportSnippet>
package com.github.kewei1.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.function.Consumer;

import com.azure.core.credential.AccessToken;
import com.azure.core.credential.TokenRequestContext;
import com.azure.identity.ClientSecretCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.azure.identity.DeviceCodeCredential;
import com.azure.identity.DeviceCodeCredentialBuilder;
import com.azure.identity.DeviceCodeInfo;
import com.microsoft.graph.authentication.IAuthenticationProvider;
import com.microsoft.graph.authentication.TokenCredentialAuthProvider;
import com.microsoft.graph.models.*;
import com.microsoft.graph.requests.DriveCollectionPage;
import com.microsoft.graph.requests.GraphServiceClient;
import com.microsoft.graph.requests.MessageCollectionPage;
import com.microsoft.graph.requests.UserCollectionPage;

import okhttp3.Request;
// </ImportSnippet>

public class Graph {

    private static DeviceCodeCredential _deviceCodeCredential;
    private static GraphServiceClient<Request> _userClient;




    //app.clientId=a4462d21-8f59-4c43-a760-abecaf791603
    //app.clientSecret=YOUR_CLIENT_SECRET_HERE_IF_USING_APP_ONLY
    //app.tenantId=YOUR_TENANT_ID_HERE_IF_USING_APP_ONLY
    //app.authTenant=common
    //app.graphUserScopes=user.read,mail.read,mail.send

    private static String clientId = "a4462d21-8f59-4c43-a760-abecaf791603";
    private static String clientSecret ="YOUR_CLIENT_SECRET_HERE_IF_USING_APP_ONLY";
    private static String tenantId = "YOUR_TENANT_ID_HERE_IF_USING_APP_ONLY";
    private static String authTenantId = "common";
    private static String graphUserScopes = "user.read,mail.read,mail.send,files.read.all,files.readwrite.all,sites.read.all,sites.readwrite.all";







    public static void initializeGraphForUserAuth( Consumer<DeviceCodeInfo> challenge) throws Exception {



        String[] split = graphUserScopes.split(",");
        final List<String> graphUserScopes = Arrays
            .asList(split);



        _deviceCodeCredential = new DeviceCodeCredentialBuilder()
            .clientId(clientId)
            .tenantId(authTenantId)
            .challengeConsumer(challenge)
            .build();

        final TokenCredentialAuthProvider authProvider =
            new TokenCredentialAuthProvider(graphUserScopes, _deviceCodeCredential);

        _userClient = GraphServiceClient.builder()
            .authenticationProvider(authProvider)
            .buildClient();
    }












    // </UserAuthConfigSnippet>

    // <GetUserTokenSnippet>
    public static String getUserToken() throws Exception {
        // Ensure credential isn't null
        if (_deviceCodeCredential == null) {
            throw new Exception("Graph has not been initialized for user auth");
        }
        String[] split = graphUserScopes.split(",");

        final String[] graphUserScopes = split;

        final TokenRequestContext context = new TokenRequestContext();
        context.addScopes(graphUserScopes);

        final AccessToken token = _deviceCodeCredential.getToken(context).block();
        return token.getToken();
    }
    // </GetUserTokenSnippet>

    // <GetUserSnippet>
    public static User getUser() throws Exception {
        // Ensure client isn't null
        if (_userClient == null) {
            throw new Exception("Graph has not been initialized for user auth");
        }

        return _userClient.me()
            .buildRequest()
            .select("displayName,mail,userPrincipalName")
            .get();
    }
    // </GetUserSnippet>

    // <GetInboxSnippet>
    public static MessageCollectionPage getInbox() throws Exception {
        // Ensure client isn't null
        if (_userClient == null) {
            throw new Exception("Graph has not been initialized for user auth");
        }

        return _userClient.me()
            .mailFolders("inbox")
            .messages()
            .buildRequest()
            .select("from,isRead,receivedDateTime,subject")
            .top(25)
            .orderBy("receivedDateTime DESC")
            .get();
    }
    // </GetInboxSnippet>

    // <SendMailSnippet>
    public static void sendMail(String subject, String body, String recipient) throws Exception {
        // Ensure client isn't null
        if (_userClient == null) {
            throw new Exception("Graph has not been initialized for user auth");
        }

        // Create a new message
        final Message message = new Message();
        message.subject = subject;
        message.body = new ItemBody();
        message.body.content = body;
        message.body.contentType = BodyType.TEXT;

        final Recipient toRecipient = new Recipient();
        toRecipient.emailAddress = new EmailAddress();
        toRecipient.emailAddress.address = recipient;

        List<Recipient> recipients = new ArrayList<>();
        recipients.add(toRecipient);
        message.toRecipients = recipients;
        // Send the message
        _userClient.me()
            .sendMail(UserSendMailParameterSet.newBuilder()
                .withMessage(message)
                .build())
            .buildRequest()
            .post();
    }
    // </SendMailSnippet>

    // <AppOnyAuthConfigSnippet>
    private static ClientSecretCredential _clientSecretCredential;
    private static GraphServiceClient<Request> _appClient;

    private static void ensureGraphForAppOnlyAuth() throws Exception {


        if (_clientSecretCredential == null) {

            _clientSecretCredential = new ClientSecretCredentialBuilder()
                .clientId(clientId)
                .tenantId(tenantId)
                .clientSecret(clientSecret)
                .build();
        }
        List<String> list = new ArrayList<>();
        list.add("https://graph.microsoft.com/.default");

        if (_appClient == null) {
            final TokenCredentialAuthProvider authProvider =
                new TokenCredentialAuthProvider(

                        list

                        , _clientSecretCredential);

            _appClient = GraphServiceClient.builder()
                .authenticationProvider(authProvider)
                .buildClient();
        }
    }
    // </AppOnyAuthConfigSnippet>

    // <GetUsersSnippet>
    public static UserCollectionPage getUsers() throws Exception {
        ensureGraphForAppOnlyAuth();

        return _appClient.users()
            .buildRequest()
            .select("displayName,id,mail")
            .top(25)
            .orderBy("displayName")
            .get();
    }
    // </GetUsersSnippet>



    //drives
    public static DriveCollectionPage getDrives() throws Exception {
        ensureGraphForAppOnlyAuth();
        getDrive();
        return _appClient.drives()
            .buildRequest()
            .select("id,name")
            .top(25)
            .orderBy("name")
            .get();
    }









    public static void getDrive() throws Exception {
        Drive drive = _appClient.me().drive()
                .buildRequest()
                .get();

        System.out.println(drive.id);
    }








    // <MakeGraphCallSnippet>
    public static void makeGraphCall() {
        // INSERT YOUR CODE HERE
        // Note: if using _appClient, be sure to call ensureGraphForAppOnlyAuth
        // before using it.
        // ensureGraphForAppOnlyAuth();
    }
    // </MakeGraphCallSnippet>
}
