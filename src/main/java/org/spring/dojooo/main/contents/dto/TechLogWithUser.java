package org.spring.dojooo.main.contents.dto;

import org.spring.dojooo.main.contents.domain.TechLog;
import org.spring.dojooo.main.users.domain.User;

public record TechLogWithUser(TechLog techLog, User user) {
}
