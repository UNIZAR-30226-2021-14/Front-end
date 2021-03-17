import React, {useState} from 'react'
import add from '../imagenes/add.png'
import cajaFuerte from '../imagenes/cajaFuerte.png'
import Popup from './Popup.js'
import 'bootstrap/dist/css/bootstrap.min.css'
import axios from 'axios'

class CuerpoDocumentos extends React.Component {

  constructor(props){
    super(props)
    this.state = {
      isFetch: true,
      showPopup:false
    }
    this.subirArchivos=this.subirArchivos.bind(this);
  }
  subirArchivos(e){
    let file = e.target.files[0];
    console.log(file);
    
    if (file) {
      let data = new FormData();
      data.append('file', file);
      // axios.post('/files', data)...
    }
  }
  togglePopup() {
    this.setState({
      showPopup: !this.state.showPopup
    });
  }
  render () {
    const { isFectch} = this.state
    return (
        <>
        {
          isFectch && 'Loading...' //Si es true entonces muestra loading... Si loading fuera componente se pondría <Loading/> 
        }
        <div className="cajaFuerte">
          <img className="caja" src={cajaFuerte} alt="cajaFuerte" />
        </div>
        <div className="textoImagen">
          <p>No hay ningún contenido</p>
          <p>Haga click en el icono + de la derecha</p>
        </div>
       
        <button className="addButton" onClick={this.togglePopup.bind(this)}><img className="add" src={add} alt="add" /></button>
        {this.state.showPopup ? 
          <Popup
            text='Selecciona los ficheros:'
            cuerpo={<input type="file" name="files" multiple onChange={this.subirArchivos}/>}
            sendData={<button type="Submit" className="Send" onClick={() => this.postData()}>Send</button>}
            closePopup={this.togglePopup.bind(this)}
          />
          : null
        }
      </>
    )
  }
}

export default CuerpoDocumentos