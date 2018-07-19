CREATE TABLE special_addresses (
  id      SERIAL PRIMARY KEY,
  address VARCHAR(42) NOT NULL,
  name    TEXT        NOT NULL,
  slug    TEXT        NOT NULL,
  type    VARCHAR(10) NOT NULL,
  chain   VARCHAR(10) DEFAULT 'MAINNET',
  url     TEXT        DEFAULT NULL
);

INSERT INTO special_addresses (address, type, name, slug)
VALUES ('0xb794f5ea0ba39494ce839613fffba74279579268', 'wallet', 'Poloniex Cold Wallet', 'coldwallet_poloniex');

INSERT INTO special_addresses (address, type, name, slug)
VALUES ('0xe853c56864a2ebe4576a807d26fdc4a0ada51919', 'wallet', 'Kraken Wallet', 'wallet_kraken');

INSERT INTO special_addresses (address, type, name, slug)
VALUES ('0xde0b295669a9fd93d5f28d9ec85e40f4cb697bae', 'wallet', 'EthDev', 'ethereum_developers');

INSERT INTO special_addresses (address, type, name, slug)
VALUES ('0x00a651d43b6e209f5ada45a35f92efc0de3a5184', 'wallet', '200M Trader', '200m_trader');

INSERT INTO special_addresses (address, type, name, slug)
VALUES ('0xfbb1b73c4f0bda4f67dca266ce6ef42f520fbb98', 'wallet', 'Bittrex', 'bittrex');

INSERT INTO special_addresses (address, type, name, slug)
VALUES ('0xf0160428a8552ac9bb7e050d90eeade4ddd52843', 'wallet', 'Digix Crowdsale', 'crowdsale_digix');

INSERT INTO special_addresses (address, type, name, slug)
VALUES ('0x7da82c7ab4771ff031b66538d2fb9b0b047f6cf9', 'wallet', 'Golem Multisig', 'multisig_golem');

INSERT INTO special_addresses (address, type, name, slug)
VALUES ('0xcafe1a77e84698c83ca8931f54a755176ef75f2c', 'wallet', 'Aragon Multisig', 'multisig_aragon');

INSERT INTO special_addresses (address, type, name, slug)
VALUES ('0xbf4ed7b27f1d666546e30d74d50d173d20bca754', 'wallet', 'DAO Withdrawal', 'dao_withdrawal');

INSERT INTO special_addresses (address, type, name, slug)
VALUES ('0x851b7f3ab81bd8df354f0d7640efcd7288553419', 'wallet', 'Gnosis Multisig', 'multisig_gnosis');

INSERT INTO special_addresses (address, type, name, slug)
VALUES ('0xc78310231aa53bd3d0fea2f8c705c67730929d8f', 'wallet', 'SingularDTV Wallet', 'wallet_singulardtv');

INSERT INTO special_addresses (address, type, name, slug)
VALUES ('0x32be343b94f860124dc4fee278fdcbd38c102d88', 'wallet', 'Poloniex Wallet', 'wallet_poloniex');

INSERT INTO special_addresses (address, type, name, slug)
VALUES ('0x0a869d79a7052c7f1b55a8ebabbea3420f0d1e13', 'wallet', 'Kraken Wallet', 'wallet_kraken');

INSERT INTO special_addresses (address, type, name, slug)
VALUES ('0x4fdd5eb2fb260149a3903859043e962ab89d8ed4', 'wallet', 'Bitfinex Wallet', 'wallet_bitfinex');

INSERT INTO special_addresses (address, type, name, slug)
VALUES ('0xdd76b55ee6dafe0c7c978bff69206d476a5b9ce7', 'wallet', 'RequestNetwork Multisig', 'multisig_requestnetwork');

INSERT INTO special_addresses (address, type, name, slug)
VALUES ('0x185f19b43d818e10a31be68f445ef8edcb8afb83', 'wallet', 'TenX Multisig', 'multisig_tenx');

INSERT INTO special_addresses (address, type, name, slug)
VALUES ('0x3eb01b3391ea15ce752d01cf3d3f09dec596f650', 'wallet', 'Kyber Multisig', 'multisig_kyber');

INSERT INTO special_addresses (address, type, name, slug)
VALUES ('0xb3764761e297d6f121e79c32a65829cd1ddb4d32', 'wallet', 'MultisigExploit Hacker', 'hacker_multisig');

INSERT INTO special_addresses (address, type, name, slug)
VALUES
  ('0x755cdba6ae4f479f7164792b318b2a06c759833b', 'wallet', 'Extrabalance DAO Withdrawal', 'dao_extrabalance_withdraw');

