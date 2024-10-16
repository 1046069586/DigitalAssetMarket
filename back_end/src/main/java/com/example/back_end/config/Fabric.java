package com.example.back_end.config;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.concurrent.TimeoutException;
import java.util.Set;
import java.security.PrivateKey;

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

@Component
@Slf4j
public class Fabric {

    static {
        System.setProperty("org.hyperledger.fabric.sdk.service_discovery.as_localhost", "true");
        System.setProperty("otel.metrics.exporter", "none");
    }


    private Fabric() throws Exception{

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
    

    public String mint(String assetURI, String name) throws IOException{
        String result;
        Path walletDirectory = Paths.get("wallet");
        Wallet wallet = Wallets.newFileSystemWallet(walletDirectory);

        // Path to a common connection profile describing the network.
        Path networkConfigFile = Paths.get("..", "..", "test-network", "organizations", "peerOrganizations",
                "org1.example.com", "connection-org1.yaml"); // connection.json

        // Configure the gateway connection used to access the network.
        Gateway.Builder builder = Gateway.createBuilder()
                .identity(wallet, name)
                .networkConfig(networkConfigFile); // .discovery(true)
        try (Gateway gateway = builder.connect()) {
            // Obtain a smart contract deployed on the network.
            Network network = gateway.getNetwork("mychannel");
            // Contract contract = network.getContract("nfr");
            Contract contract = network.getContract("nfr");
            // byte[] mintResponse = contract.createTransaction("mint").submit(assetURI, name);
            byte[] mintResponse = contract.submitTransaction("mint", assetURI, name); // !!!!!!!!!!!!!
            result = new String(mintResponse, StandardCharsets.UTF_8);
        } catch(ContractException | TimeoutException | InterruptedException e) {
            e.printStackTrace();
            result = "Transaction Failed";
        }
        System.out.println(result);
        return result;
    }

    public String transfer(String assetID, String rightURI, String name) throws IOException{
        String result;
		Path walletDirectory = Paths.get("wallet");
        Wallet wallet = Wallets.newFileSystemWallet(walletDirectory);

        // Path to a common connection profile describing the network.
        Path networkConfigFile = Paths.get("..", "..", "test-network", "organizations", "peerOrganizations",
                "org1.example.com", "connection-org1.yaml"); // connection.json

        // Configure the gateway connection used to access the network.
        Gateway.Builder builder = Gateway.createBuilder()
                .identity(wallet, name)
                .networkConfig(networkConfigFile); // .discovery(true)
        try (Gateway gateway = builder.connect()) {
            // Obtain a smart contract deployed on the network.
            Network network = gateway.getNetwork("mychannel");
            Contract contract = network.getContract("nfr");
            byte[] transferResponse = contract.createTransaction("transfer").submit(assetID, rightURI, name);
            result = new String(transferResponse, StandardCharsets.UTF_8);
        } catch(ContractException | TimeoutException | InterruptedException e) {
            e.printStackTrace();
            result = "Transaction Failed";
        }
        System.out.println(result);
        return result;
    }

    public String query(String key, String name) throws IOException{
        String result;
		Path walletDirectory = Paths.get("wallet");
        Wallet wallet = Wallets.newFileSystemWallet(walletDirectory);

        // Path to a common connection profile describing the network.
        Path networkConfigFile = Paths.get("..", "..", "test-network", "organizations", "peerOrganizations",
                "org1.example.com", "connection-org1.yaml"); // connection.json

        // Configure the gateway connection used to access the network.
        Gateway.Builder builder = Gateway.createBuilder()
                .identity(wallet, name)
                .networkConfig(networkConfigFile); // .discovery(true)
        try (Gateway gateway = builder.connect()) {
            // Obtain a smart contract deployed on the network.
            Network network = gateway.getNetwork("mychannel");
            Contract contract = network.getContract("nfr");
            byte[] queryResponse = contract.evaluateTransaction("query", key);
            result = new String(queryResponse, StandardCharsets.UTF_8);
        } catch(ContractException e) {
            e.printStackTrace();
            result = "Transaction Failed";
        }
        System.out.println(result);
        return result;
    }

    public String queryAll(String name) throws IOException{
        String result;
		Path walletDirectory = Paths.get("wallet");
        Wallet wallet = Wallets.newFileSystemWallet(walletDirectory);

        // Path to a common connection profile describing the network.
        Path networkConfigFile = Paths.get("..", "..", "test-network", "organizations", "peerOrganizations",
                "org1.example.com", "connection-org1.yaml"); // connection.json

        // Configure the gateway connection used to access the network.
        Gateway.Builder builder = Gateway.createBuilder()
                .identity(wallet, name)
                .networkConfig(networkConfigFile); // .discovery(true)
        try (Gateway gateway = builder.connect()) {
            // Obtain a smart contract deployed on the network.
            Network network = gateway.getNetwork("mychannel");
            Contract contract = network.getContract("nfr");
            byte[] queryResponse = contract.evaluateTransaction("queryAll");
            result = new String(queryResponse, StandardCharsets.UTF_8);
        } catch(ContractException e) {
            e.printStackTrace();
            result = "Transaction Failed";
        }
        System.out.println(result);
        log.info(result);
        return result;
    }
    
}
