import React from 'react'
import logo from '../imagenes/logo.jpg'
import {Link} from  'react-router-dom'

class Cabecera2 extends React.Component {
    constructor (props) {
        super(props)
    }
    render () {
        return (
            <table className="cabecera">
              <tbody>
                <tr>
                  <td><Link to="/">
                    <img className="logoEmpresa" src={logo} alt="Logo"/>
                  </Link></td>
                  <td><Link to="/paginaSecundaria/seguridad">
                    <button className="seguridad">Seguridad</button>
                  </Link></td>
                  <td><Link to="/paginaSecundaria/contraseñas">
                    <button className="contraseñas">Contraseñas</button>
                  </Link></td>
                  <td><Link to="/paginaSecundaria/documentos">
                    <button className="documentos">Documentos</button>
                  </Link></td>
                  <td><Link to="/paginaSecundaria/imagenes">
                    <button className="imagenes">Imagenes</button>
                  </Link></td>
                </tr>
              </tbody>
              
            </table>
        );
    }
}
export default Cabecera2