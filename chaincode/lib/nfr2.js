/*
 * Copyright IBM Corp. All Rights Reserved.
 *
 * SPDX-License-Identifier: Apache-2.0
 */

'use strict';

const { Contract } = require('fabric-contract-api');
const MintTx = require('./mintTx.js')
const TransferTx = require('./transferTx.js')


class NFR extends Contract {

    constructor(){
        super("nfr")
        this.nfrCount = 0
    }

    async mint(ctx, assetURI, userName){
        // 生成ID编号
        this.nfrCount += 1
        let assetID = this.nfrCount
        let creator = userName
        // 获取此次交易的地址
        let key = ctx.stub.getTxID()
        let data = new MintTx({assetID, assetURI, creator})
        // 保存此次铸造交易信息至区块链数据库
        let result = await ctx.stub.putState(key, JSON.stringify(data));
        // 记录该NFR的最新交易地址
        await ctx.stub.putState(String(assetID), key)
        // 返回该NFR编号
        return assetID
    }

    async transfer(ctx, assetID, rightURI, userName){
        // 获取此次交易的地址
        let key = ctx.stub.getTxID()
        // 获取该NFR最新交易地址
        let latestTxID = await ctx.stub.getState(String(assetID))
        latestTxID = latestTxID.toString()
        // 获取最新交易的记录
        let latestTx = await ctx.stub.getState(latestTxID)
        latestTx = JSON.parse(latestTx.toString())

        let assetAddress
        let from
        // 判断最新交易是否为铸造交易并为from、assetAddress赋值
        if(latestTx.assetAddress){
            assetAddress = latestTx.assetAddress
            from = latestTx.to
        }else{
            assetAddress = latestTxID
            from = 0
        }
        // 原最新交易变为前一次交易
        let preTransaction = latestTxID
        let to = userName
        let data = new TransferTx({assetAddress, from, to, preTransaction, rightURI})
         // 保存此次转移交易信息至区块链数据库
        let result = await ctx.stub.putState(key, JSON.stringify(data))
         // 更新该NFR的最新交易地址为此次的转移交易
        await ctx.stub.putState(String(assetID), key)
        return key
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
    
    async txTrace(ctx, assetID){
        const allTx = [] 
        let latest = await ctx.stub.getState(String(assetID))
        let latestTx = await ctx.stub.getState(latest.toString())
        latestTx = JSON.parse(latestTx.toString())
        allTx.push(latestTx)
        while(latestTx.assetAddress){
            latestTx = await ctx.stub.getState(latestTx.preTransaction)
            latestTx = JSON.parse(latestTx.toString())
            allTx.push(latestTx)
        }
        return JSON.stringify(allTx)
    
    }

}

module.exports = NFR
