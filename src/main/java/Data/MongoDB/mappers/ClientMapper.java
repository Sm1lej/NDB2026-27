package Data.MongoDB.mappers;

import Data.MongoDB.documents.ClientDocument;
import model.Client;

public class ClientMapper {
    public Client toDomain(ClientDocument clientDoc) {
        if (clientDoc == null) {
            return null;
        }

        Client client = new Client(
                clientDoc.getName(),
                clientDoc.getLastname(),
                clientDoc.getPesel(),
                clientDoc.getEmail(),
                clientDoc.getPhoneNumber(),
                clientDoc.getClientType()
        );

        client.setArchive(clientDoc.isArchive());

        return client;
    }

    public ClientDocument toDocument(Client client) {
        if (client == null) {
            return null;
        }

        ClientDocument clientDoc = new ClientDocument();

        clientDoc.setPesel(client.getPesel());
        clientDoc.setName(client.getName());
        clientDoc.setLastname(client.getLastName());
        clientDoc.setEmail(client.getEmail());
        clientDoc.setPhoneNumber(client.getPhoneNumber());
        clientDoc.setClientType(client.getClientType());
        clientDoc.setArchive(client.isArchive());

        return clientDoc;
    }
}
