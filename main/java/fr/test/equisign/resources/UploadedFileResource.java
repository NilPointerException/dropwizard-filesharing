package fr.test.equisign.resources;

import fr.test.equisign.dao.UploadedFileDAO;
import fr.test.equisign.entity.UploadedFile;
import fr.test.equisign.utils.CryptoUtils;
import io.dropwizard.hibernate.UnitOfWork;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;


@Path("/files")
@Produces(APPLICATION_JSON)
public class UploadedFileResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(UploadedFileResource.class);
    private static final String UPLOADED_FILE_DIR = "./uploadedFiles/";
    private final UploadedFileDAO uploadedFileDAO;

    public UploadedFileResource(UploadedFileDAO uploadedFileDAO) {
        this.uploadedFileDAO = uploadedFileDAO;
    }

    @POST
    @UnitOfWork
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(APPLICATION_JSON)
    public Response post(@FormDataParam("file") File filedata,
                         @FormDataParam("filename") String filename,
                         @FormDataParam("owner") String owner) {

        File ownerDir = new File(UPLOADED_FILE_DIR + owner);
        if (!ownerDir.exists()) {
            ownerDir.mkdirs();
        }
        File dest = new File(UPLOADED_FILE_DIR + owner + "/" + filename + ".encrypted");

        try {
            CryptoUtils.encrypt(filedata, dest);
            LOGGER.info("Fichier chiffré:" + filename);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }

        UploadedFile newFile = new UploadedFile(filedata, filename, owner, dest.getAbsolutePath());
        uploadedFileDAO.create(newFile);

        return Response
                .status(Response.Status.OK)
                .entity("Fichier envoyé")
                .build();
    }

    @GET
    @UnitOfWork
    @Path("/owner/{owner}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFileByOwner(@PathParam("owner") String owner) {
        List<UploadedFile> dbFiles = uploadedFileDAO.findByOwner(owner);

        final GenericEntity<List<UploadedFile>> entity
                = new GenericEntity<List<UploadedFile>>(dbFiles) {
        };

        return Response.ok(entity).build();
    }

    @GET
    @UnitOfWork
    @Path("/sluginfo/{slug}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFileInfoBySlug(@PathParam("slug") String slug) {
        UploadedFile dbFile = uploadedFileDAO.findBySlug(slug);

        final GenericEntity<UploadedFile> entity
                = new GenericEntity<UploadedFile>(dbFile) {
        };

        return Response.ok(entity).build();
    }

    @GET
    @UnitOfWork
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getFileById(@PathParam("id") long id) {
        UploadedFile dbFile = uploadedFileDAO.findById(id);
        File cryptedFile = new File(UPLOADED_FILE_DIR + dbFile.getOwner() + "/" + dbFile.getFilename() + ".encrypted");
        File decryptedFile = new File(UPLOADED_FILE_DIR + dbFile.getOwner() + "/" + dbFile.getFilename());

        try {
            CryptoUtils.decrypt(cryptedFile, decryptedFile);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }

        return Response.ok(decryptedFile, MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", "attachment; filename=\"" + dbFile.getFilename() + "\"")
                .build();
    }

    @DELETE
    @UnitOfWork
    @Path("/{id}")
    @Produces(APPLICATION_JSON)
    public Response deleteById(@PathParam("id") long id) {
        UploadedFile dbFile = uploadedFileDAO.findById(id);
        // TODO
        // uploadedFileDAO.deleteById(id);

        File cryptedFile = new File(UPLOADED_FILE_DIR + dbFile.getOwner() + "/" + dbFile.getFilename() + ".encrypted");
        if (cryptedFile.delete()) {
            LOGGER.info("File deleted successfully");
            return Response
                    .status(Response.Status.OK)
                    .entity("Fichier supprimé")
                    .build();
        } else {
            LOGGER.info("Failed to delete the file");
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("Erreur lors de la suppression")
                    .build();
        }

    }

    @GET
    @UnitOfWork
    @Path("/slug/{slug}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getFileBySlug(@PathParam("slug") String slug) {
        UploadedFile dbFile = uploadedFileDAO.findBySlug(slug);
        File cryptedFile = new File(UPLOADED_FILE_DIR + dbFile.getOwner() + "/" + dbFile.getFilename() + ".encrypted");
        File decryptedFile = new File(UPLOADED_FILE_DIR + dbFile.getOwner() + "/" + dbFile.getFilename());

        try {
            CryptoUtils.decrypt(cryptedFile, decryptedFile);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }

        return Response.ok(decryptedFile, MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", "attachment; filename=\"" + dbFile.getFilename() + "\"")
                .build();
    }

    @GET
    @UnitOfWork
    @Path("/all")
    @Produces(APPLICATION_JSON)
    public Response getAllFiles() {
        List<UploadedFile> dbFiles = uploadedFileDAO.findAll();

        final GenericEntity<List<UploadedFile>> entity
                = new GenericEntity<List<UploadedFile>>(dbFiles) {
        };

        return Response.ok(entity).build();
    }
}
