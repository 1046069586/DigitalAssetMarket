
class TransferTx {

    constructor(obj){
        Object.assign(this, obj)
    }
    
    getAssetAddress(){
        return this.assetAddress
    }

    getFrom(){
        return this.from
    }

    getTo(){
        return this.to
    }

    getPreTransaction(){
        return this.preTransaction
    }

    getRightURI(){
        return this.rightURI
    }
}

module.exports = TransferTx