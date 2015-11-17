package pl.koznik.configuration

import javax.persistence.Persistence

trait DatabaseTest {
  protected val entityManager = Persistence.createEntityManagerFactory("integration-test").createEntityManager()
  protected val transaction = entityManager.getTransaction

  protected def transactional(func: () => Unit) = {
    transaction.begin()
    func.apply()
    transaction.commit()
  }
}
