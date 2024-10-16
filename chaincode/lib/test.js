/*
 * Copyright IBM Corp. All Rights Reserved.
 *
 * SPDX-License-Identifier: Apache-2.0
 */

'use strict';

const { Contract } = require('fabric-contract-api');
//const MintTx = require('./mintTx.js')
//const TransferTx = require('./transferTx.js')


class Test extends Contract {

    constructor(){
        super("test")
        this.assetCount = 0
        this.nfrCount = 0
        this.nftCount = 0
    }

    async mintNFR(ctx, assetURL, creator){
        const assetID = ++this.assetCount;
        const NFRID = ++this.nfrCount;
        const mintEvent = { assetID: assetID, NFRID: NFRID, assetURL: assetURL, creator: creator };
        ctx.stub.setEvent('Mint', Buffer.from(JSON.stringify(mintEvent)));
        const latestTx = ctx.stub.getTxID();
        const nfrData = { assetID: assetID, owner: creator, rightURL: null, latestTx: latestTx };
        await ctx.stub.putState(String(NFRID), Buffer.from(JSON.stringify(nfrData)));
        return NFRID;
    }

    async transferNFR(ctx, NFRID, rightURL, to){
        let nfrData= await ctx.stub.getState(String(NFRID));
        nfrData = JSON.parse(nfrData.toString());
        const assetID = nfrData.assetID;
        const preTransaction = nfrData.latestTx;
        let from = nfrData.owner;
        if(from == to){
            from = 0;
        }
        if(rightURL != nfrData.rightURL && nfrData.rightURL != null){
            NFRID = ++this.nfrCount;
        }
        const transferEvent = { assetID: assetID, NFRID: NFRID, from: from, to: to, preTransaction: preTransaction, rightURL: rightURL };
        ctx.stub.setEvent('Transfer', Buffer.from(JSON.stringify(transferEvent)));
        const latestTx = ctx.stub.getTxID();
        nfrData = { assetID: assetID, owner: to, rightURL: rightURL, latestTx: latestTx };
        await ctx.stub.putState(String(NFRID), Buffer.from(JSON.stringify(nfrData)));
        return NFRID;
    } 
    
    async latestTxOf(ctx, NFRID){
    	let nfrData= await ctx.stub.getState(String(NFRID));
        nfrData = JSON.parse(nfrData.toString());
        const latestTx = nfrData.latestTx;
        return latestTx;
    }
    
    async mintNFT(ctx, creator){
    	const tokenId = ++this.nftCount;
    	const transferEvent = { from: 0, to: creator, tokenId: String(tokenId) };
    	ctx.stub.setEvent('Transfer', Buffer.from(JSON.stringify(transferEvent)));
    	return tokenId;
    }
    
    async transferNFT(ctx, tokenId, from, to){
    	const transferEvent = { from: from, to: to, tokenId: tokenId};
        ctx.stub.setEvent('Transfer', Buffer.from(JSON.stringify(transferEvent)));
    }

    async query(ctx, key){   
        let data = await ctx.stub.getState(String(key))
        return data.toString()
    }

    async queryAll(ctx) {
        const startKey = '';
        const endKey = '';
        const allResults = [];
        for await (const {key, value} of ctx.stub.getStateByRange(startKey, endKey)) {
            const strValue = Buffer.from(value).toString('utf8');
            let record;
            try {
                record = JSON.parse(strValue);
            } catch (err) {
                console.log(err);
                record = strValue;
            }
            allResults.push({ Key: key, Record: record });
        }
        console.info(allResults);
        return JSON.stringify(allResults);
    }
    

}

module.exports = Test
