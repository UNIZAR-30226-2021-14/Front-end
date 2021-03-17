import React from 'react'
import CopyR from './CopyR'
import Cabecera2 from './Cabecera2'
import Cuerpo2 from './Cuerpo2.js'
import CuerpoContraseña from './CuerpoContraseña.js'
import CuerpoDocumentos from './CuerpoDocumentos.js'
import CuerpoImagenes from './CuerpoImagenes.js'
import JoinForm from './JoinForm.js'
import CuerpoSeguridad from './CuerpoSeguridad.js'
import { BrowserRouter as Router, Switch, Route} from 'react-router-dom'

class PaginaSecundaria extends React.Component {
  constructor (props) {
      super(props)
  }

  render () {
    return (
      <React.Fragment> 
        <Cabecera2/>
        <Switch>
          <Route path="/paginaSecundaria/seguridad" component={CuerpoSeguridad} />
          <Route path="/paginaSecundaria/contraseñas" component={CuerpoContraseña} />
          <Route path="/paginaSecundaria/documentos" component={CuerpoDocumentos} />
          <Route path="/paginaSecundaria/imagenes" component={CuerpoImagenes} />
        </Switch>
        <CopyR>© 2021 Fetch Tech. Todos los derechos reservados. Marca comercial.</CopyR>
       </React.Fragment>

      )
  }
}

export default PaginaSecundaria