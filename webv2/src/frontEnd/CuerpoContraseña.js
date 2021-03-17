import React from 'react'
import add from '../imagenes/add.png'
import cajaFuerte from '../imagenes/cajaFuerte.png'

class CuerpoContraseña extends React.Component {

  constructor(props){
    super(props)
    this.state = {
      isFetch: true,
    }
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
      <button className="addButton"><img className="add" src={add} alt="add" /></button>
    </>
    )
  }
}

export default CuerpoContraseña
