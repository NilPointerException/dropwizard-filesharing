import React, {useEffect} from "react";
import {Home} from "./components/home/Home";
import {Filesharing} from "./components/filesharing/Filesharing";
import {
    BrowserRouter as Router,
    Switch,
    Route,
} from "react-router-dom";
import axios from "axios";
import fileDownload from "js-file-download";

function App() {
    return (
        <Router>
            <div>
                <Switch>
                    <Route path="/slugdl">
                        <SlugDownloader/>
                    </Route>
                    <Route path="/filesharing">
                        <Filesharing/>
                    </Route>
                    <Route exact path="/">
                        <Home/>
                    </Route>
                </Switch>
            </div>
        </Router>
    );
}

function SlugDownloader() {

    useEffect(() => {
        let slug = window.location.pathname.split("/").pop();
        infoRequest(slug)
    })

    const infoRequest = (slug) => {
        fetch('http://localhost:9000/files/sluginfo/' + slug)
            .then(res => res.json())
            .then(data => {
                console.log(data)
                downloadRequest(data)
            })
    }

    const downloadRequest = (file) => {
        axios.get('http://localhost:9000/files/slug/' + file.slug, {
            responseType: 'blob',
        })
            .then((res) => {
                fileDownload(res.data, file.filename)
            });
    }

    return <h1>Le téléchargement commence, merci de votre visite.</h1>

}
export default App;