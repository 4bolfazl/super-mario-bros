package ir.sharif.math.ap2023.project.model;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

@SuppressWarnings("resource")
public class JPAService {
    private static JPAService instance;
    private final EntityManager em;

    private JPAService() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("super-mario-bros");
        em = emf.createEntityManager();
    }

    public static JPAService getInstance() {
        if (instance == null)
            instance = new JPAService();
        return instance;
    }

    // TODO: Add database communication methods
}
