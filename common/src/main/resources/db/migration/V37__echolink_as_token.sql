delete from custom_erc20 where address = '0xa6a840e50bcaa50da017b91a0d86b8b2d41156ee';

INSERT INTO tokens (name, slug, address, website, social, symbol, decimals, coinmarketcap_name)
VALUES ('EchoLink', 'echolink', '0xa6a840e50bcaa50da017b91a0d86b8b2d41156ee', 'https://echolink.info/',
        'https://twitter.com/EchoLinkInfo1', 'EKO', 18, 'echolink');