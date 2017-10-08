package aor.demo.crud.services;


import aor.demo.auth.PasswordEncoderProvider;
import aor.demo.crud.entities.Client;
import aor.demo.crud.entities.Example;
import aor.demo.crud.entities.PlatformUser;
import aor.demo.crud.repos.ClientRepository;
import aor.demo.crud.repos.ExampleRepository;
import aor.demo.crud.repos.UserRepository;
import aor.demo.crud.utils.ApiHandler;
import aor.demo.crud.utils.JSON;
import com.google.common.base.CaseFormat;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletContext;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class DataInitService {

    @Autowired
    private ExampleRepository exampleRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoderProvider passEncodeProvider;

    @Autowired
    ServletContext context;

    @Autowired
    ApiHandler apiHandler;


    public void init() {





//        UploadFile file = new UploadFile();
//        file.path = "C:\\Users\\Michael\\Documents\\GitHub\\aor-demo\\src\\main\\webapp\\WEB-INF\\uploaded\\ParentMapper.txt";
//        fileRepository.save(file);
//
//        UploadFile fileRef = new UploadFile();
//        fileRef.id = 1;


//
//        Client client = new Client();
//        client.name = "client 1";
//        client.password = passEncodeProvider.getEncoder().encode("client1");
//        client.username = "client1";
//        clientRepository.save(client);
//
//        Client clientRef = new Client();
//        clientRef.id = 1;
//
//
//        Example e1 = new Example();
//        e1.client = clientRef;
//
//
//        Example e2 = new Example();
//        e2.client = clientRef;
//
//        exampleRepository.save(e1);
//        exampleRepository.save(e2);


        PlatformUser admin = new PlatformUser();
        admin.username = "demo";
        admin.password = passEncodeProvider.getEncoder().encode("demo");
        userRepository.save(admin);


        File dataFile = new File(context.getRealPath("/WEB-INF/uploaded/data.js"));
        FileInputStream fis = null;
        JSONObject jsonObj = null;
        try {
            fis = new FileInputStream(dataFile);
            byte[] data = new byte[(int) dataFile.length()];
            fis.read(data);
            fis.close();
            String dataStr = new String(data, "UTF-8");
            jsonObj = JSON.toJsonObject(dataStr);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Set<String> keys = jsonObj.keySet();
        String token = apiHandler.authenticate("demo", "demo");
        Map<String, String> headers = new HashMap<>();
        headers.put("X-Authorization", "Bearer "+token);
        for (String key : keys) {
            System.out.println(key);
            if (key.equals("categories") ||
                key.equals("customers") || key.equals("products")
                ) {
                JSONArray objects = ((JSONArray)jsonObj.get(key));
                for (int i = 0; i < objects.length(); i++) {
                    JSONObject object = objects.getJSONObject(i);
                    apiHandler.sendPost("http://localhost:8080/api/v1/"+key+"/",object.toString(), headers);
                }
            }
        }
    }
}
