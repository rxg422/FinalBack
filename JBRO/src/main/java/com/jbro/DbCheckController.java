package com.jbro;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DbCheckController {

	private final JdbcTemplate jdbcTemplate;

	public DbCheckController(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@GetMapping("/api/health/db")
	public Map<String, Object> checkDb() {
		Integer result = jdbcTemplate.queryForObject("SELECT 1", Integer.class);

		return Map.of(
			"ok", true,
			"database", "mysql",
			"result", result
		);
	}

	@GetMapping("/api/health/tables")
	public Map<String, Object> listTables() {
		List<String> tables = jdbcTemplate.queryForList("SHOW TABLES", String.class);

		return Map.of(
			"ok", true,
			"tables", tables
		);
	}

	@GetMapping("/api/health/tables/{tableName}")
	public Map<String, Object> readTable(@PathVariable String tableName) {
		if (!tableName.matches("[A-Za-z0-9_]+")) {
			throw new IllegalArgumentException("Invalid table name");
		}

		List<String> tables = jdbcTemplate.queryForList("SHOW TABLES", String.class);
		if (!tables.contains(tableName)) {
			throw new IllegalArgumentException("Table not found: " + tableName);
		}

		List<Map<String, Object>> rows = jdbcTemplate.queryForList("SELECT * FROM `" + tableName + "` LIMIT 20");

		return Map.of(
			"ok", true,
			"table", tableName,
			"rows", rows
		);
	}
}
