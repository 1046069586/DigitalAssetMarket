'use strict'

class MintTx {

    constructor(obj){
        Object.assign(this, obj)
    }
    
    getAssetID(){
        return this.assetID
    }

    getAssetURI(){
        return this.assetURI
    }

    getCreator(){
        return this.creator
    }
}

module.exports = MintTx