import React,{ Component }from 'react'
import logo from '../imagenes/logo.jpg'
import Popup from './Popup.js'
import {Link} from  'react-router-dom'
import PropTypes from 'prop-types';
import { Redirect } from 'react-router';
import PasswordRevealer from './PasswordRevealer'

const validate = values =>{
  const errors = {}
  if (!values.usuario){
    errors.usuario = 'Ingrese un usuario válido'
  }
  if (!values.contrasenya){
    errors.contrasenya = 'Ingrese una contraseña válida'
  }
  return errors

}

class Cabecera extends React.Component {
    constructor (props) {
        super(props)
        this.state = {
          showPopup1:false,
          showPopup2:false,
          showPopup3:false,
          usuario:'',
          contrasenya:'',
          errors: {},
          redireccion:false,
          shown:false
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
    handleChange = ({target}) => {
      const{name,value} = target
      this.setState({[name]:value})
    }
    handleSubmit =e => {
      e.preventDefault()
      //Así separo errors del resto de estado
      const {errors, ...sinErrors} = this.state
      const result = validate(sinErrors)
      
      this.setState({errors:result})
      if(!Object.keys(result).length){ //Si tiene propiedades, hay error
        //Envio formulario
        console.log('Formulario válido')
        this.postData()
        this.setState({redireccion:true})
      }else{
        console.log('Formulario inválido')
      }

    }
  


    
  
    render () {
    
        const {errors}=this.state
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
                cuerpo = {
                  <form onSubmit={this.handleSubmit}>
                    <div className="formularioLogin">
                      <label htmlFor="usuario">Usuario</label>
                      <input type="text" name="usuario"id="usuario" onChange={this.handleChange}/>
                      {errors.usuario && <p className="warning">{errors.usuario}</p>}
                      <br/>
                      <label htmlFor="contrasenya">Contraseña</label>
                      <input type='password' name = "contrasenya"id="contrasenya"onChange={this.handleChange}/>
                      {errors.contrasenya && <p className="warning">{errors.contrasenya}</p>}
                    
                      <br/>
                     <input type='submit' className="Send" value='Enviar'/>
                     {this.state.redireccion && <Redirect to="/paginaSecundaria"/>}
                  
                    </div>
                    </form>
                }
                closePopup={this.togglePopup2.bind(this)}
             />
            : null
            }
            {this.state.showPopup3 ? 
              <Popup
                text='Registrate'
                cuerpo = {
                  <form onSubmit={this.handleSubmit}>
                  <div className="formularioLogin">
                    <label htmlFor="usuario">Usuario</label>
                    <input type="text" name="usuario"id="usuario" onChange={this.handleChange}/>
                    {errors.usuario && <p className="warning">{errors.usuario}</p>}
                    <br/>
                    <label htmlFor="contrasenya">Contraseña</label>
                    <input type="password" name = "contrasenya"id="contrasenya"onChange={this.handleChange}/>
                    {errors.contrasenya && <p className="warning">{errors.contrasenya}</p>}
                    <br/>
                   <input type='submit' className="Send" value='Enviar'/>
                   {this.state.redireccion && <Redirect to="/paginaSecundaria"/>}
                
                  </div>
                  </form>
                }
                closePopup={this.togglePopup3.bind(this)}
             />
            : null
            }
            </>
          )
    }

}

export default Cabecera




