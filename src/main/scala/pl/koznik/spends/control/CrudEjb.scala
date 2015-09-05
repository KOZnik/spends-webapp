package pl.koznik.spends.control

import javax.persistence.{EntityManager, PersistenceContext}

trait CrudEjb[E] {

  @PersistenceContext
  protected var manager: EntityManager = _

  def create(entity: E): E = {
    manager persist entity
    entity
  }

  def findByNamedQuery(queryName: String, parameters: Map[String, AnyRef] = Map.empty): java.util.List[E] = {
    val query = manager.createNamedQuery(queryName, manifest.runtimeClass)
    parameters.foreach { case (key: String, value: AnyRef) => query.setParameter(key, value) }
    query.getResultList.asInstanceOf[java.util.List[E]]
  }

  def read(id: Long)(implicit manifest: Manifest[E]): E = manager.find(manifest.runtimeClass, id).asInstanceOf[E]

  def update(entity: E): E = manager merge entity

  def delete(entity: E) = manager remove entity
}