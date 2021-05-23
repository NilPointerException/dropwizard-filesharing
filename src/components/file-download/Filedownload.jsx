import React, {useState, useEffect} from "react";
import fileDownload from 'js-file-download'
import axios from 'axios'

export function Filedownload() {
    const [newUserFiles, setNewUserFiles] = useState([]);

    // fetch data
    useEffect(() => {
        let owner = window.location.pathname.split("/").pop();
        fetch('http://localhost:9000/files/owner/' + owner)
            .then(res => res.json())
            .then(data => {
                let userFiles = []
                data.map((file) => {
                    userFiles.push(file)
                    return true
                })
                setNewUserFiles(userFiles)
            })
    }, [])

    const differenceWithToday = (timestamp) => {
        let today = new Date();
        let uploadDate = new Date(timestamp * 1000);
        let diffMs = (uploadDate - today);
        let diffHrs = Math.floor((diffMs % 86400000) / 3600000); // hours
        let diffMins = Math.round(((diffMs % 86400000) % 3600000) / 60000); // minutes
        if (diffMins < 60) {
            return "Il y a moins d'une heure"
        }
        if (diffHrs < 24) {
            return "Il y a " + diffHrs + " heures"
        }
    }

    const convertSizeToHumanReadable = (sizeInBytes) => {
        let mb = Math.trunc((sizeInBytes / 1024) / 1024)
        if (mb < 1) {
            return "< 1Mo"
        } else {
            return mb + " Mo"
        }
    }

    const handleDownloadClick = (event) => {
        axios.get('http://localhost:9000/files/' + event.target.dataset.id, {
            responseType: 'blob',
        })
            .then((res) => {
                fileDownload(res.data, event.target.dataset.filename)
            });
    }

    const handleSlugClick = (event) => {
        alert(window.location.origin + "/slugdl/" + event.target.dataset.slug)
    }

    return <div className={"container"}>
        <h2> > Télécharger mes fichiers</h2>
        {newUserFiles.length === 0 ?
            <h3>Aucun fichier pour le moment...</h3> :
            <table id="download-table">
                <thead>
                <tr>
                    <th>Télécharger</th>
                    <th>Nom du fichier</th>
                    <th>Taille du fichier</th>
                    <th>Date de mise en ligne</th>
                </tr>
                </thead>
                <tbody>

                {newUserFiles.map(file => (
                    <tr key={file.id}>
                        <td className={"actions-td"}>
                            <button data-id={file.id} data-filename={file.filename} onClick={handleDownloadClick}>
                                <i className="fa fa-download"></i> Télécharger
                            </button>
                            <button data-slug={file.slug} onClick={handleSlugClick}>
                                <i className="fa fa-share"></i>Créer un lien de partage
                            </button>
                        </td>
                        <td>{file.filename}</td>
                        <td>{convertSizeToHumanReadable(file.size)}</td>
                        <td>{differenceWithToday(file.uploaddate)}</td>
                    </tr>
                ))}
                </tbody>
            </table>
        }


    </div>
}

export default Filedownload;