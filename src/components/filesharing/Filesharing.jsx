import FileUpload from "../file-upload/file-upload.component";
import Filedownload from "../file-download/Filedownload";
import React, {useState} from "react";


export function Filesharing() {
    const [newUserFiles, setNewUserInfo] = useState({
        userFiles: []
    });

    const updateUploadedFiles = (files) =>
        setNewUserInfo({...newUserFiles, userFiles: files});
    // console.log(newUserFiles)

    const handleSubmit = (event) => {
        event.preventDefault();
        //logic to create new user...
    };

    return <div>
        <form onSubmit={handleSubmit}>
            <FileUpload
                accept="*"
                label=""
                updateFilesCb={updateUploadedFiles}
            />
        </form>
        <Filedownload/>
    </div>
}