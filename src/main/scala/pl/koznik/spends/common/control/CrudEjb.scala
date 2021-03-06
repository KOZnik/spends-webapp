package pl.koznik.spends.common.control

import javax.persistence.{EntityManager, LockModeType, NoResultException, PersistenceContext}

import pl.koznik.spends.common.control.Converters._

trait CrudEjb[E] {

  @PersistenceContext
  protected var manager: EntityManager = _

  def create(entity: E): E = {
    manager persist entity
    entity
  }

  def findOneByNamedQuery(queryName: String, parameters: Map[String, AnyRef] = Map.empty): Option[E] = {
    val query = manager.createNamedQuery(queryName, manifest.runtimeClass)
    query.setLockMode(LockModeType.OPTIMISTIC)
    parameters.foreach { case (key: String, value: AnyRef) => query.setParameter(key, value) }
    try {
      Option.apply(query.getSingleResult.asInstanceOf[E])
    } catch {
      case e: NoResultException => Option.empty[E]
    }
  }

  def findByNamedQuery(queryName: String, parameters: Map[String, AnyRef] = Map.empty): List[E] = {
    val query = manager.createNamedQuery(queryName, manifest.runtimeClass)
    query.setLockMode(LockModeType.OPTIMISTIC)
    parameters.foreach { case (key: String, value: AnyRef) => query.setParameter(key, value) }
    query.getResultList.asInstanceOf[java.util.List[E]]
  }

  def read(id: Long)(implicit manifest: Manifest[E]): E = manager.find(manifest.runtimeClass, id, LockModeType.OPTIMISTIC).asInstanceOf[E]

  def update(entity: E): E = {
    manager.lock(entity, LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    manager merge entity
  }

  def delete(entity: E) = manager remove entity
}