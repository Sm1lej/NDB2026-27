package Data.mappers;

import model.Client;
import Data.entity.ClientEnt;

public class ClientMapper {
    public Client toDomain(ClientEnt clientEnt) {
        if (clientEnt == null) {
            throw new IllegalArgumentException("ClientEnt jest nullem");
        }

        Client client = new Client(
                clientEnt.getName(),
                clientEnt.getLastName(),
                clientEnt.getPesel(),
                clientEnt.getEmail(),
                clientEnt.getPhoneNumber(),
                clientEnt.getClientType()
        );

        client.setArchive(clientEnt.isArchive());
        return client;
    }

    public ClientEnt toEntity(Client client) {
        if (client == null) {
            throw new IllegalArgumentException("Client jest nullem");
        }

        ClientEnt clientEnt = new ClientEnt();

        clientEnt.setName(client.getName());
        clientEnt.setLastName(client.getLastName());
        clientEnt.setPesel(client.getPesel());
        clientEnt.setEmail(client.getEmail());
        clientEnt.setPhoneNumber(client.getPhoneNumber());
        clientEnt.setClientType(client.getClientType());


        return clientEnt;
    }

}
