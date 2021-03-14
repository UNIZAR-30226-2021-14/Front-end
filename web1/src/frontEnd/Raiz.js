import React from 'react'
import PaginaInicial from './PaginaInicial'
import Cuerpo from './Cuerpo'
import CopyR from './CopyR'
import { BrowserRouter as Router, Switch, Route} from 'react-router-dom'
import PaginaSecundaria from './PaginaSecundaria'


function Raiz() {
  return ( 
  <Router>
    <Switch>
      <Route path="/" exact component={PaginaInicial} />
      <Route path="/paginaSecundaria" component={PaginaSecundaria} />
    </Switch>
  </Router>
    
  );
}



export default Raiz