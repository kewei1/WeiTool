package com.github.kewei1.graph;

import cn.hutool.core.stream.StreamUtil;
import cn.hutool.core.util.CharsetUtil;
import com.alibaba.fastjson.JSONObject;
import com.azure.core.credential.AccessToken;
import com.azure.core.credential.TokenRequestContext;
import com.azure.identity.*;
import com.microsoft.graph.authentication.TokenCredentialAuthProvider;
import com.microsoft.graph.models.Drive;
import com.microsoft.graph.models.DriveItem;
import com.microsoft.graph.models.User;
import com.microsoft.graph.requests.DriveCollectionPage;
import com.microsoft.graph.requests.DriveItemCollectionPage;
import com.microsoft.graph.requests.GraphServiceClient;
import com.microsoft.graph.requests.UserCollectionPage;
import okhttp3.Request;

import java.io.*;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class oneDrive {

    private static String clientId = "a4462d21-8f59-4c43-a760-abecaf791603";
    private static String clientSecret ="YOUR_CLIENT_SECRET_HERE_IF_USING_APP_ONLY";
    private static String tenantId = "YOUR_TENANT_ID_HERE_IF_USING_APP_ONLY";
    private static String authTenantId = "common";
    private static String graphUserScopes = "user.read,mail.read,mail.send,files.read.all,files.readwrite.all,sites.read.all,sites.readwrite.all";


    //Value  hoC8Q~UMips6fpit3V3oDS_p.8EPd88n2H6VDclG
    //Secret ID 1e6c8035-2409-4af5-875a-e4ca86d3ae66
    private static ClientSecretCredential _clientSecretCredential;

    private static GraphServiceClient<Request> _appClient;

    private static DeviceCodeCredential _deviceCodeCredential;
    private static GraphServiceClient<Request> _userClient;

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

    public static void ClientSecretCredential(){

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
                    new TokenCredentialAuthProvider(list, _clientSecretCredential);


            _appClient = GraphServiceClient.builder()
                    .authenticationProvider(authProvider)
                    .buildClient();
        }
    }



    public static void main(String[] args) throws Exception{
        try {
            initializeGraphForUserAuth(
                    challenge -> System.out.println(challenge.getMessage()));
        } catch (Exception e)
        {
            System.out.println("Error initializing Graph for user auth");
            System.out.println(e.getMessage());
        }

        ClientSecretCredential();

        greetUser();

        Scanner input = new Scanner(System.in);

        int choice = -1;

        while (choice != 0) {
            System.out.println("请选择以下选项之一:");
            System.out.println("0. 退出");
            System.out.println("1. 获取用户令牌");
            System.out.println("2. 获取用户信息:");

            try {
                choice = input.nextInt();
            } catch (InputMismatchException ex) {
                // Skip over non-integer input
            }

            input.nextLine();

            // Process user choice
            switch(choice) {
                case 0:
                    // Exit the program
                    System.out.println("Goodbye...");
                    break;
                case 1:
                    // Display access token
                    getUserToken();
                    break;
                case 2:
                    // Display access token
                    greetUser();
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        }

        input.close();




    }






    //GET /me/drive
    public static void getDrive() throws Exception {
        Drive drive = _userClient.me().drive().buildRequest().get();

    }

    //GET /drive
    public static void getDrive2() throws Exception {
        DriveCollectionPage driveCollectionPage = _userClient.drives().buildRequest().get();

    }

    //GET /me/drive/root/children
    public static void getDriveRootChildren() throws Exception {
        DriveItemCollectionPage driveItemCollectionPage = _userClient.me().drive().root().children().buildRequest().get();

    }















    private static void greetUser() {

        try {
            final User user = _userClient.me()
                    .buildRequest()
                    .select("displayName,mail,userPrincipalName")
                    .get();
            // For Work/school accounts, email is in mail property
            // Personal accounts, email is in userPrincipalName
            final String email = user.mail == null ? user.userPrincipalName : user.mail;
            System.out.println("Hello, " + user.displayName + "!");
            System.out.println("Email: " + email);
        } catch (Exception e) {
            System.out.println("Error getting user");
            System.out.println(e.getMessage());
        }
    }


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

}
