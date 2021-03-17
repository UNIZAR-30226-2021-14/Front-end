import React from 'react'
import add from '../imagenes/add.png'
import cajaFuerte from '../imagenes/cajaFuerte.png'

class Cuerpo2 extends React.Component {

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
      <p>CUERPO PAGINA 2</p>
     </>
    )
  }
}

export default Cuerpo2
