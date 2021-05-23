package fr.test.equisign.entity;

import javax.persistence.*;
import java.io.File;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "files")
//@NamedQueries(
//        {
//                @NamedQuery(
//                        name = "fr.test.equisign.entity.UploadedFile.findAll",
//                        query = "SELECT p FROM UploadedFile p"
//                ),
//                @NamedQuery(
//                        name = "fr.test.equisign.entity.UploadedFile.findByOwner",
//                        query = "SELECT p FROM UploadedFile p WHERE p.owner = :owner"
//                )
//        })
public class UploadedFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "filename", nullable = false)
    private String filename;

    @Column(name = "path", nullable = false)
    private String path;

    @Column(name = "size", nullable = false)
    private long size;

    @Column(name = "owner")
    private String owner;

    @Column(name = "uploaddate")
    private Date uploaddate;

    @Column(name = "slug")
    private String slug;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Date getUploaddate() {
        return uploaddate;
    }

    public void setUploaddate(Date uploaddate) {
        this.uploaddate = uploaddate;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    UploadedFile() {
    }

    public UploadedFile(File sourceFile, String filename, String owner, String path) {
        this.setFilename(filename);
        this.setOwner(owner);
        this.setSize(sourceFile.length());
        this.setUploaddate(new Date());
        this.setPath(path);
        this.setSlug(UUID.randomUUID().toString());
    }
}
