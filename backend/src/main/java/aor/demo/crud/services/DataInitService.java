package aor.demo.crud.services;


import aor.demo.auth.PasswordEncoderProvider;
import aor.demo.crud.Client;
import aor.demo.crud.ExampleEntity;
import aor.demo.crud.PlatformUser;
import aor.demo.crud.repos.ClientRepository;
import aor.demo.crud.repos.ExampleRepository;
import aor.demo.crud.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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


    public void init() {


//        UploadFile file = new UploadFile();
//        file.path = "C:\\Users\\Michael\\Documents\\GitHub\\aor-demo\\src\\main\\webapp\\WEB-INF\\uploaded\\ParentMapper.txt";
//        fileRepository.save(file);
//
//        UploadFile fileRef = new UploadFile();
//        fileRef.id = 1;



        Client client = new Client();
        client.name = "client 1";
        client.password = passEncodeProvider.getEncoder().encode("client1");
        client.username = "client1";
        clientRepository.save(client);

        Client clientRef = new Client();
        clientRef.id = 1;


        ExampleEntity e1 = new ExampleEntity();
        e1.dateStarted = "2017-02-22T22:00:00.000Z";
        e1.client = clientRef;


        ExampleEntity e2 = new ExampleEntity();
        e2.dateStarted = "2016-02-21T22:00:00.000Z";
        e2.client = clientRef;
        e2.isResolved = true;

        exampleRepository.save(e1);
        exampleRepository.save(e2);


        PlatformUser admin = new PlatformUser();
        admin.username = "demo";
        admin.password = passEncodeProvider.getEncoder().encode("demo");
        userRepository.save(admin);












    }
}
