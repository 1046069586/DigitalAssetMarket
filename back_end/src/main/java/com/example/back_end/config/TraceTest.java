package com.example.back_end.config;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
// import java.util.Collection;
import java.util.Properties;
import java.util.concurrent.TimeoutException;
import java.util.Set;
import java.security.PrivateKey;
import java.util.regex.Matcher;  
import java.util.regex.Pattern; 

import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.ContractException;
import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.Network;
import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.gateway.Wallets;
import org.hyperledger.fabric.gateway.Identities;
import org.hyperledger.fabric.gateway.Identity;
import org.hyperledger.fabric.gateway.X509Identity;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric.sdk.security.CryptoSuiteFactory;
import org.hyperledger.fabric_ca.sdk.EnrollmentRequest;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.hyperledger.fabric_ca.sdk.RegistrationRequest;
import org.hyperledger.fabric.sdk.User;

// import org.hyperledger.fabric.sdk.ProposalResponse;
// import org.hyperledger.fabric.sdk.QueryByChaincodeRequest;

@Component
@Slf4j
public class TraceTest {

    static {
        System.setProperty("org.hyperledger.fabric.sdk.service_discovery.as_localhost", "true");
        System.setProperty("otel.metrics.exporter", "none");
    }

    private TraceTest() throws Exception{

        // Create a CA client for interacting with the CA.
		Properties props = new Properties();
		props.put("pemFile",
			"../../test-network/organizations/peerOrganizations/org1.example.com/ca/ca.org1.example.com-cert.pem");
		props.put("allowAllHostNames", "true");
		HFCAClient caClient = HFCAClient.createNewInstance("https://localhost:7054", props);
		CryptoSuite cryptoSuite = CryptoSuiteFactory.getDefault().getCryptoSuite();
		caClient.setCryptoSuite(cryptoSuite);
		// Create a wallet for managing identities
		Wallet wallet = Wallets.newFileSystemWallet(Paths.get("wallet"));

		// Check to see if we've already enrolled the admin user.
		if (wallet.get("admin") != null) {
			System.out.println("An identity for the admin user \"admin\" already exists in the wallet");
			return;
		}

		// Enroll the admin user, and import the new identity into the wallet.
		final EnrollmentRequest enrollmentRequestTLS = new EnrollmentRequest();
		enrollmentRequestTLS.addHost("localhost");
		enrollmentRequestTLS.setProfile("tls");
		Enrollment enrollment = caClient.enroll("admin", "adminpw", enrollmentRequestTLS);
		Identity user = Identities.newX509Identity("Org1MSP", enrollment);
		wallet.put("admin", user);
		System.out.println("Successfully enrolled user \"admin\" and imported it into the wallet");
    }

    public void register(String name) throws Exception {
        // Create a CA client for interacting with the CA.
		Properties props = new Properties();
		props.put("pemFile",
			"../../test-network/organizations/peerOrganizations/org1.example.com/ca/ca.org1.example.com-cert.pem");
		props.put("allowAllHostNames", "true");
		HFCAClient caClient = HFCAClient.createNewInstance("https://localhost:7054", props);
		CryptoSuite cryptoSuite = CryptoSuiteFactory.getDefault().getCryptoSuite();
		caClient.setCryptoSuite(cryptoSuite);

		// Create a wallet for managing identities
		Wallet wallet = Wallets.newFileSystemWallet(Paths.get("wallet"));

		// Check to see if we've already enrolled the user.
		if (wallet.get(name) != null) {
			System.out.println("An identity for the user \""+ name + "\" already exists in the wallet");
			return;
		}

		X509Identity adminIdentity = (X509Identity)wallet.get("admin");
		if (adminIdentity == null) {
			System.out.println("\"admin\" needs to be enrolled and added to the wallet first");
			return;
		}
		User admin = new User() {

			@Override
			public String getName() {
				return "admin";
			}

			@Override
			public Set<String> getRoles() {
				return null;
			}

			@Override
			public String getAccount() {
				return null;
			}

			@Override
			public String getAffiliation() {
				return "org1.department1";
			}

			@Override
			public Enrollment getEnrollment() {
				return new Enrollment() {

					@Override
					public PrivateKey getKey() {
						return adminIdentity.getPrivateKey();
					}

					@Override
					public String getCert() {
						return Identities.toPemString(adminIdentity.getCertificate());
					}
				};
			}

			@Override
			public String getMspId() {
				return "Org1MSP";
			}

		};

		// Register the user, enroll the user, and import the new identity into the wallet.
		RegistrationRequest registrationRequest = new RegistrationRequest(name);
		registrationRequest.setAffiliation("org1.department1");
		registrationRequest.setEnrollmentID(name);
		String enrollmentSecret = caClient.register(registrationRequest, admin);
		Enrollment enrollment = caClient.enroll(name, enrollmentSecret);
		Identity user = Identities.newX509Identity("Org1MSP", enrollment);
		wallet.put(name, user);
		System.out.println("Successfully enrolled user \"" + name + "\" and imported it into the wallet");
    }

    public void dataGen(int assetNum, int txNum) throws IOException{
        Path walletDirectory = Paths.get("wallet");
        Wallet wallet = Wallets.newFileSystemWallet(walletDirectory);

        // Path to a common connection profile describing the network.
        Path networkConfigFile = Paths.get("..", "..", "test-network", "organizations", "peerOrganizations",
                "org1.example.com", "connection-org1.yaml"); // connection.json

        // Configure the gateway connection used to access the network.
        Gateway.Builder builder = Gateway.createBuilder()
                .identity(wallet, "appUser")
                .networkConfig(networkConfigFile); // .discovery(true)
        try (Gateway gateway = builder.connect()) {
            // Obtain a smart contract deployed on the network.
            Network network = gateway.getNetwork("mychannel");
            Contract contract = network.getContract("test");
            for(int i = 1; i <= assetNum; i++){
                String assetURL = "url" + i;
                String creator = "user1";
                byte[] response1 = contract.submitTransaction("mintNFR", assetURL, creator);
                String NFRID = new String(response1, StandardCharsets.UTF_8);
                byte[] response2 = contract.submitTransaction("mintNFT", creator);
                String NFTID = new String(response2, StandardCharsets.UTF_8);
                String rightURL = "rightURL" + i;
                for(int j = 1; j <= txNum; j++){
                    String NFRto = "user" + j;
                    response1 = contract.submitTransaction("transferNFR", NFRID, rightURL, NFRto);
                    String NFTfrom = "user" + j;
                    String NFTto = "user" + (j+1);
                    response2 = contract.submitTransaction("transferNFT", NFTID, NFTfrom, NFTto);
                }
            }
        } catch(ContractException | TimeoutException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Long NFRtxTrace(String NFRID) throws IOException{
		long start = 0, end = 0;

        Path walletDirectory = Paths.get("wallet");
        Wallet wallet = Wallets.newFileSystemWallet(walletDirectory);

        // Path to a common connection profile describing the network.
        Path networkConfigFile = Paths.get("..", "..", "test-network", "organizations", "peerOrganizations",
                "org1.example.com", "connection-org1.yaml"); // connection.json

        // Configure the gateway connection used to access the network.
        Gateway.Builder builder = Gateway.createBuilder()
                .identity(wallet, "appUser")
                .networkConfig(networkConfigFile); // .discovery(true)
        try (Gateway gateway = builder.connect()) {
            start = System.currentTimeMillis();
            Network network = gateway.getNetwork("mychannel");
            Contract contract = network.getContract("test");
            byte[] response1 = contract.submitTransaction("latestTxOf", NFRID);
            String txID = new String(response1, StandardCharsets.UTF_8);
            Contract qscc = network.getContract("qscc");
            String regex = "\"preTransaction\":\"([^,]*)\",\"rightURL\""; 
            Pattern pattern = Pattern.compile(regex); 
            while(true){
                byte[] response2 = qscc.evaluateTransaction("GetTransactionByID", "mychannel", txID);
                String tx = new String(response2, StandardCharsets.UTF_8);
                Matcher matcher = pattern.matcher(tx);  
                if (matcher.find()) {  
                    String preTx = matcher.group(1);  
                    txID = preTx;  
                } else {  
                    break;  
                }  
            }
			end = System.currentTimeMillis(); 
        } catch(ContractException | TimeoutException | InterruptedException e) {
            e.printStackTrace();
        }
        
        log.info("NFR tx trace time:" + (end - start));
		return (end - start);
    }

	public Long ERCtxTrace(String NFTID, String lastUser) throws IOException{
        // Transfer"-{"from":"user1","to":"user2","tokenId":"NaN"}�"
		long start = 0L, end = 0L;
        int i = 0;
        Path walletDirectory = Paths.get("wallet");
        Wallet wallet = Wallets.newFileSystemWallet(walletDirectory);
        Path networkConfigFile = Paths.get("..", "..", "test-network", "organizations", "peerOrganizations",
                "org1.example.com", "connection-org1.yaml"); // connection.json

        // Configure the gateway connection used to access the network.
        Gateway.Builder builder = Gateway.createBuilder()
                .identity(wallet, "appUser")
                .networkConfig(networkConfigFile); // .discovery(true)
        try (Gateway gateway = builder.connect()) {
            // Obtain a smart contract deployed on the network.
            start = System.currentTimeMillis();
            Network network = gateway.getNetwork("mychannel");
            Contract contract = network.getContract("qscc");
			String regex = "\"to\":\"([^,]*)\",\"tokenId\":\"" + NFTID; 
            Pattern pattern = Pattern.compile(regex); 
            byte[] response;
            String block;
            while(true){
                response = contract.evaluateTransaction("GetBlockByNumber", "mychannel", String.valueOf(i));
                block = new String(response, StandardCharsets.UTF_8);
                Matcher matcher = pattern.matcher(block);
                if (matcher.find()) { 
                    String to = matcher.group(1);
                    if(to.equals(lastUser)){
                        break;
                    }
                }  
                i++;   
            }
            end = System.currentTimeMillis();
        } catch(ContractException  e) {
            e.printStackTrace(); 
        }
        
        log.info("ERC tx trace time:" + (end - start));
		return (end - start);	
    }

    public void blockHeight() throws IOException{
        int i = 0;
        Path walletDirectory = Paths.get("wallet");
        Wallet wallet = Wallets.newFileSystemWallet(walletDirectory);
        Path networkConfigFile = Paths.get("..", "..", "test-network", "organizations", "peerOrganizations",
                "org1.example.com", "connection-org1.yaml"); // connection.json
        Gateway.Builder builder = Gateway.createBuilder()
                .identity(wallet, "appUser")
                .networkConfig(networkConfigFile); // .discovery(true)
        try (Gateway gateway = builder.connect()) {
            Network network = gateway.getNetwork("mychannel");
            Contract contract = network.getContract("qscc");
            while(true){
                contract.evaluateTransaction("GetBlockByNumber", "mychannel", String.valueOf(i));
                i++;   
            }
        } catch(ContractException  e) {
            //e.printStackTrace();  
        }
        log.info("blockheight:" + (i-1));
    }

	public void queryAll() throws IOException{
        String result;
		Path walletDirectory = Paths.get("wallet");
        Wallet wallet = Wallets.newFileSystemWallet(walletDirectory);

        // Path to a common connection profile describing the network.
        Path networkConfigFile = Paths.get("..", "..", "test-network", "organizations", "peerOrganizations",
                "org1.example.com", "connection-org1.yaml"); // connection.json

        // Configure the gateway connection used to access the network.
        Gateway.Builder builder = Gateway.createBuilder()
                .identity(wallet, "appUser")
                .networkConfig(networkConfigFile); // .discovery(true)
        try (Gateway gateway = builder.connect()) {
            // Obtain a smart contract deployed on the network.
            Network network = gateway.getNetwork("mychannel");
            Contract contract = network.getContract("test");
            byte[] queryResponse = contract.evaluateTransaction("queryAll");
            result = new String(queryResponse, StandardCharsets.UTF_8);
        } catch(ContractException e) {
            e.printStackTrace();
            result = "Transaction Failed";
        }
        log.info(result);
    }

	public void qscc(String txID) throws IOException{
        String result;
		Path walletDirectory = Paths.get("wallet");
        Wallet wallet = Wallets.newFileSystemWallet(walletDirectory);

        // Path to a common connection profile describing the network.
        Path networkConfigFile = Paths.get("..", "..", "test-network", "organizations", "peerOrganizations",
                "org1.example.com", "connection-org1.yaml"); // connection.json

        // Configure the gateway connection used to access the network.
        Gateway.Builder builder = Gateway.createBuilder()
                .identity(wallet, "appUser")
                .networkConfig(networkConfigFile); // .discovery(true)
        try (Gateway gateway = builder.connect()) {
            // Obtain a smart contract deployed on the network.
            Network network = gateway.getNetwork("mychannel");
            Contract contract = network.getContract("qscc");
            //Collection<ProposalResponse> responses = contract.evaluateTransaction("GetTransactionByID", "mychannel", txID);
            byte[] queryResponse = contract.evaluateTransaction("GetBlockByNumber", "mychannel", "1");//GetChainInfo
            // result = Base64.getEncoder().encodeToString(queryResponse);
            // queryResponse = Base64.getDecoder().decode(result);
            result = new String(queryResponse, StandardCharsets.UTF_8);//
        } catch(ContractException e) {
            e.printStackTrace();
            result = "Transaction Failed";
        }
        log.info(result);
    }
    
}
