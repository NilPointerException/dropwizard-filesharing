import React, {useRef, useState} from "react";
import {post} from 'axios';
import {
  FileUploadContainer,
  FormField,
  DragDropText,
  UploadFileBtn,
  InputLabel
} from "./file-upload.styles";

const DEFAULT_MAX_FILE_SIZE_IN_BYTES = 12000000;

const convertNestedObjectToArray = (nestedObj) =>
  Object.keys(nestedObj).map((key) => nestedObj[key]);

const FileUpload = ({
  label,
  updateFilesCb,
  maxFileSizeInBytes = DEFAULT_MAX_FILE_SIZE_IN_BYTES,
  ...otherProps
}) => {
  const fileInputField = useRef(null);
  const [files, setFiles] = useState({});

  const handleUploadBtnClick = () => {
    fileInputField.current.click();
  };

  const addNewFiles = (newFiles) => {
    for (let file of newFiles) {
      if (file.size <= maxFileSizeInBytes) {
        if (!otherProps.multiple) {
          return { file };
        }
        files[file.name] = file;
      }
    }
    return {...files};
  };

  const callUpdateFilesCb = (files) => {
    const filesAsArray = convertNestedObjectToArray(files);
    updateFilesCb(filesAsArray);
  };

  const fileUpload = (file) => {
    const url = 'http://localhost:9000/files';
    const owner = window.location.pathname.split("/").pop();
    const formData = new FormData();
    formData.append('file', file)
    formData.append('owner', owner)
    formData.append('filename', file.name)
    const config = {
      headers: {
        'content-type': 'multipart/form-data'
      }
    }
    return post(url, formData, config)
  }

  const handleNewFileUpload = (e) => {
    const {files: newFiles} = e.target;
    if (newFiles.length) {
      let updatedFiles = addNewFiles(newFiles)
      setFiles(updatedFiles)
      callUpdateFilesCb(updatedFiles)
      fileUpload(newFiles[0]).then(window.location.reload())

    }
  };

  return (
    <>
      <h2> > Envoyer un fichier</h2>
      <FileUploadContainer>
        <InputLabel>{label}</InputLabel>
        <DragDropText style={{color: "black", margin: "10px"}}>Déposer un fichier ici (maximum 20Mo)</DragDropText>
        <UploadFileBtn type="button" onClick={handleUploadBtnClick}>
          <i className="fas fa-file-upload"/>
          <span> Choisir un fichier</span>
        </UploadFileBtn>
        <FormField
            type="file"
            ref={fileInputField}
          onChange={handleNewFileUpload}
          title=""
          value=""
          {...otherProps}
        />
      </FileUploadContainer>
    </>
  );
};

export default FileUpload;
