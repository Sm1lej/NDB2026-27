package jparepositories;

import entity.ClientEnt;
import jakarta.persistence.EntityManager;
import java.util.List;

public class JpaClientRepository {
    private EntityManager em;
    public JpaClientRepository(EntityManager em) {
        this.em = em;
    }
    public void add(ClientEnt clientEnt){
        if(clientEnt == null){
            throw new NullPointerException("ClientEnt is null");
        }
        em.persist(clientEnt);
    }
    public void remove(ClientEnt clientEnt){
        if(clientEnt == null){
            throw new NullPointerException("ClientEnt is null");
        }
        em.remove(clientEnt);
    }
    public ClientEnt findbyPhoneNumber(String phoneNumber){
        if(phoneNumber == null){
            throw new NullPointerException("Phone number is null");
        }
        List<ClientEnt> entities = em.createQuery(
                "select c from ClientEnt c where c.phoneNumber = :phoneNumber", ClientEnt.class).setParameter("phoneNumber", phoneNumber).getResultList();
        if(entities.isEmpty()){
            return null;
        }
        return entities.get(0);
    }
}
