ALTER TABLE veiculo
	ADD placa VARCHAR(8) NOT NULL DEFAULT 'foo';
	
ALTER TABLE veiculo ALTER COLUMN placa DROP DEFAULT;