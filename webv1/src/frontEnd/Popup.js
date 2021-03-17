import React from 'react'
import { Button, ModalHeader,ModalBody, ModalFooter } from 'reactstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
import PropTypes from 'prop-types'

class Popup extends React.Component {
    constructor (props) {
        super()
    }
   
    render() {
      return (
        <div className='popup'>
          <div className='popup_inner'>
            <ModalHeader cssModule={{'modal-title': 'w-100 text-center'}}>
                <div className="titulo">{this.props.text}</div>
            </ModalHeader>
            <ModalBody>
                <div>{this.props.cuerpo}</div>
            </ModalBody>
            <ModalFooter>
                <Button onClick={this.props.closePopup}>close me</Button>
                <div>{this.props.sendData}</div>
            </ModalFooter>
          </div>
        </div>
      );
    }
  }

Popup.propTypes = {
    text:PropTypes.string.isRequired, //esto hace que name no sea opcional
    closePopup:PropTypes.func.isRequired,
    cuerpo:PropTypes.object.isRequired
  }
  export default Popup
  

  