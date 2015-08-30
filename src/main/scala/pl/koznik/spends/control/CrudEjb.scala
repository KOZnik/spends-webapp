package pl.koznik.spends.control

import javax.persistence.{EntityManager, PersistenceContext}

trait CrudEjb[E] {

  @PersistenceContext
  protected var manager: EntityManager = _

  def create(entity: E): E = {
    manager persist entity
    entity
  }

  def findByNamedQuery(queryName : String) : java.util.List[E] = manager.createNamedQuery(queryName, manifest.runtimeClass).getResultList.asInstanceOf[java.util.List[E]]

  //def readAll()(implicit m: Manifest[E]) : java.util.List[E] = manager createNamedQuery ("findAll" + m.runtimeClass.getSimpleName, classOf[E]) getResultList

  def read(id: Long)(implicit manifest: Manifest[E]): E = manager.find(manifest.runtimeClass, id).asInstanceOf[E]

  def update(entity: E): E = manager merge entity

  def delete(entity: E) { manager remove entity }
}