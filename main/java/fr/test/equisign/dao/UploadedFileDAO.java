package fr.test.equisign.dao;

import fr.test.equisign.entity.UploadedFile;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

public class UploadedFileDAO extends AbstractDAO<UploadedFile> {
    public UploadedFileDAO(SessionFactory factory) {
        super(factory);
    }

    public UploadedFile findById(Long id) {
        return get(id);
    }

    public List<UploadedFile> findByOwner(String owner) {
        Query<UploadedFile> query = super.query("SELECT f FROM UploadedFile f WHERE f.owner = '" + owner + "'");
        return query.getResultList();
    }

    public UploadedFile create(UploadedFile uploadedFile) {
        return persist(uploadedFile);
    }

    public List<UploadedFile> findAll() {
        Query<UploadedFile> query = super.query("SELECT f FROM UploadedFile f");
        return query.getResultList();
    }

    public UploadedFile findBySlug(String slug) {
        Query<UploadedFile> query = super.query("SELECT f FROM UploadedFile f WHERE f.slug = '" + slug + "'");
        return query.uniqueResult();
    }

//    TODO : requete de delete
}