package com.irostec.boardgamemanager.application.core.createuser.dependency;

import com.irostec.boardgamemanager.application.core.api.transaction.Transaction1;
import com.irostec.boardgamemanager.application.core.createuser.error.CreateUserError;
import com.irostec.boardgamemanager.common.type.ValidatedUsername;
import com.irostec.boardgamemanager.application.core.createuser.output.SavedUser;

public interface SaveUser extends Transaction1<CreateUserError, SavedUser,ValidatedUsername> {
}
