/*
 * Copyright IBM Corp. All Rights Reserved.
 *
 * SPDX-License-Identifier: Apache-2.0
 */

'use strict';

const { Contract } = require('fabric-contract-api');

const nfrPrefix = "nfr"
const ercPrefix = "erc"

class Test extends Contract{

    constructor(){
        super("test")
        this.txID = 1
    }

    // NFR 型的数据生成
    async NFRdataGen(ctx, assetNum, TxNum){
    	let key = ctx.stub.getTxID()
        for(let i = 1; i <= assetNum; i++){
            let mintData = {
                assetID: String(i), 
                assetURI: "URI" + i,
                creator: "user" + i
            }
            let assetAddr =  this.txID
            await ctx.stub.putState(String(assetAddr), JSON.stringify(mintData))
            for(let j = 1; j <= TxNum; j++){
                let transferData = {
                    assetAddress: String(assetAddr), 
                    from: "user" + j, 
                    to: "user" + (j+1), 
                    preTransaction: String(this.txID), 
                    rightURI: "rightURI" + j
                }
                this.txID += 1
                await ctx.stub.putState(String(this.txID), JSON.stringify(transferData))
                
            }
            let latest = this.txID
            let key = nfrPrefix + i
            await ctx.stub.putState(key, String(latest))
        }
        const transferEvent = { from: "0", to: "1", tokenId: "001" };
        ctx.stub.setEvent('NFRdata', Buffer.from(JSON.stringify(transferEvent)));
        return key
    }

    // NFR型的交易溯源
    async NFRtxTrace(ctx, assetID){
        //let start = performance.now()
        let key = nfrPrefix + assetID
        let latest = await ctx.stub.getState(key)
        let latestTx = await ctx.stub.getState(latest.toString())
        latestTx = JSON.parse(latestTx.toString())
        while(latestTx.assetAddress){
            latestTx = await ctx.stub.getState(latestTx.preTransaction)
            latestTx = JSON.parse(latestTx.toString())
        }
        //let end = performance.now()
        //let result = end - start
        return true
    }

    // ERC721型数据生成
    async ERCdataGen(ctx, assetNum, TxNum){
    	let key = ctx.stub.getTxID()
        for(let i = 1; i <= assetNum; i++){
            let mintData = {
                from: "0", 
                to: "user1", 
                tokenId: i
            }
            await ctx.stub.putState(String(this.txID++), JSON.stringify(mintData))
            for(let j = 1; j <= TxNum; j++){
                let transferData = {
                    from: "user" + j, 
                    to: "user" + (j+1), 
                    tokenId: i
                }
                await ctx.stub.putState(String(this.txID++), JSON.stringify(transferData))
            }
            let latest = this.txID - 1
            let key = ercPrefix + i
            await ctx.stub.putState(key, String(latest))   
        }
        const transferEvent = { from: "0", to: "1", tokenId: "001" };
        ctx.stub.setEvent('ERCdata', Buffer.from(JSON.stringify(transferEvent)));
        return key
    }

    // ERC型 交易溯源
    async ERCtxTrace(ctx, assetID){
        //let start = performance.now()
        let key = ercPrefix + assetID
        let latest = await ctx.stub.getState(key)
        let latestTx = await ctx.stub.getState(latest.toString())
        latestTx = JSON.parse(latestTx.toString())
        let from = latestTx.from
        const startKey = '';
        const endKey = '';

        while(from != "0"){
            for await (const {key, value} of ctx.stub.getStateByRange(startKey, endKey)){
                let record = JSON.parse(value.toString())
                if(record.to == from && record.tokenId == assetID){
                    from = record.from
                    
                    break
                }
            }
        }
       
        //let end = performance.now()
        return true
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
