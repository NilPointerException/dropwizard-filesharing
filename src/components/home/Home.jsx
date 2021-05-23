import React from "react";
import './home.css';

export function Home() {

    const onKeyDown = (event) => {
        if (event.key === 'Enter') {
            event.preventDefault();
            event.stopPropagation();
            console.log(event.target.value)
            window.location.replace("filesharing/" + event.target.value.toLowerCase())
        }
    }


    return <form>
        <div className="fun-cube"><i className="fa fa-cube"></i></div>
        <h1>Equisign Challenge </h1>
        <h1>Envoi et récéption de fichiers</h1>

        <div id="cuboid">
            <form>
                <div>
                    <p className="cuboid-text">Entrer votre nom</p>
                </div>
                <div>
                    <label htmlFor="submit" className="submit-icon">
                        <i className="fa fa-chevron-right"></i>
                    </label>
                    <input type="text" id="name" className="cuboid-text" placeholder="Votre nom" autoComplete="off"
                           onKeyDown={onKeyDown}/>
                </div>
                <div>
                    <p className="cuboid-text loader">Un moment...</p>
                </div>
                <div>
                    <span className="reset-icon"><i className="fa fa-refresh"></i></span>
                    <p className="cuboid-text">Merci</p>
                </div>
            </form>
        </div>
        <footer style={{position: "fixed", bottom: "5px", right: "5px"}}> Copyright &copy; 2021 nilauberge.tech. All rights reserved</footer>
    </form>
}
