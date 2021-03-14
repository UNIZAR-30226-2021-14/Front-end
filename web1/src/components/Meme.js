import React from 'react'
import PropTypes from 'prop-types'


//parametros, los componentes se llaman props, de propiedades, son objetos de java script con todas las propiedades del componente
//Un componente se ve como una función que va a recibir unos parámetros
//{} para expresión java script
//Solo un elemento raíz en el mismo nivel
/*const Meme = (props) => {
  return <h2>{props.name}</h2>
}*/
//const Meme = (props) => <h2>{props.name}</h2>
//const MyH2 = ({title}) => <h2>{title}</h2>
//const Meme = ({name}) => <h2>{true ? 'true' : 'false'}</h2>
//const Meme = ({name}) => <MyH2 title={name}/>
const Meme = ({name,imageUrl,box_count}) => (
  <div className="single-meme">
    <h2>{name}</h2>
    <img src={imageUrl} alt={name}/>
    <p>box_count: {box_count}</p>

  </div>
)
Meme.propTypes = {
  name:PropTypes.string.isRequired, //esto hace que name no sea opcional
  imageUrl:PropTypes.string.isRequired,
  box_count:PropTypes.number.isRequired,
}

export default Meme