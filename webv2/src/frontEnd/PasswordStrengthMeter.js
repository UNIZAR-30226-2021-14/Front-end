import React, { Component } from 'react';
import zxcvbn from 'zxcvbn';

class PasswordStrengthMeter extends Component {


  createPasswordLabel = (result) => {
    switch (result) {
      case 0:
        return 'Debil';
      case 1:
        return 'Debil';
      case 2:
        return 'Normal';
      case 3:
        return 'Buena';
      case 4:
        return 'Segura';
      default:
        return 'Débil';
    }
  }
  nivelSeguridad = (contrasenya) => {
    
    if((/[0-9]/.test(contrasenya) && !/[a-zA-Z@#_-]/.test(contrasenya)) || ((contrasenya.length < 2)&&(contrasenya.length > 0))){
      console.log("1")
      return 1;
    }else if(/[a-z+0-9]/.test(contrasenya) && !/[A-Z@#_-]/.test(contrasenya)){
      console.log("2")
      return 2;
    }else if(/[a-zA-Z0-9]/.test(contrasenya) && !/[@#_-]/.test(contrasenya)){
      console.log("3")
    return 3;
    }else if(/[a-zA-Z0-9@#_-]/.test(contrasenya) && (contrasenya.length < 8)){
      return 3;
    
    }else if(/[a-zA-Z0-9@#_-]/.test(contrasenya) && (contrasenya.length >= 8)){
      console.log("4")
      return 4;
    }else{
      console.log("5")
      return 0;
    }

  }

  render() {
    const { password } = this.props;
    const testedResult = this.nivelSeguridad(password);
    return (
      <div className="password-strength-meter">
        <progress className={`password-strength-meter-progress strength-${this.createPasswordLabel(testedResult)}`} value={testedResult} max="4"/>
        <br />
        <label className="password-strength-meter-label">
          {password && (
            <>
              <strong>Password strength:</strong> {this.createPasswordLabel(testedResult)}
            </>
          )}
        </label>
      </div>
    );
  }
}

export default PasswordStrengthMeter;
