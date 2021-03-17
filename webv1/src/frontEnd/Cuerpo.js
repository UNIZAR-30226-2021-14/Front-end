import React from 'react'
import img1 from '../imagenes/img1.jpg'
import img2 from '../imagenes/img2.jpg'
import img3 from '../imagenes/img3.jpg'

class Cuerpo extends React.Component {

  constructor(props){
    super(props)
    this.state = {
      search: '',
      isFetch: true,
    }
  }
  async componentDidMount (){
    this.setState({search:img1, isFectch : false})
  }
  handleChange1 = (e) => {
    this.setState({search: img1})
  }
  handleChange2 = (e) => {
    this.setState({search: img2})
  }
  handleChange3 = (e) => {
    this.setState({search: img3})
  }

  render () {
    const { isFectch,search} = this.state
    return (
      <>
      {
        isFectch && 'Loading...' //Si es true entonces muestra loading... Si loading fuera componente se pondría <Loading/> 
      }
      <div className="cuerpo1">
      <div className="tercio"><img className="img1" src={search} alt="img1"/>
      <div className="botones">
        <input  id="input1" name="radio" type="radio" onChange={this.handleChange1}/>
        <input  id="input2" name="radio" type="radio" onChange={this.handleChange2}/>
        <input  id="input3" name="radio" type="radio" onChange={this.handleChange3}/>
      </div></div>
      <p className="descripcion">Esto es un gestor de contraseñas.</p>
      <p className="descripcion">Esto es un gestor de contraseñas.</p>
      <p className="descripcion">Esto es un gestor de contraseñas.</p>
      <p className="descripcion">Esto es un gestor de contraseñas.</p>
      <p className="descripcion">Esto es un gestor de contraseñas.</p>
      <p className="descripcion">Esto es un gestor de contraseñas.</p>
      <p className="descripcion">Esto es un gestor de contraseñas.</p>
      <p className="descripcion">Esto es un gestor de contraseñas.</p>
      <br></br>
      </div>
    </>
    )
  }
}

export default Cuerpo
