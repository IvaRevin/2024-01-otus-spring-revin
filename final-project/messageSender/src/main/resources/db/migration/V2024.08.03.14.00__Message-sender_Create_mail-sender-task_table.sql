CREATE TABLE "message_sender_task"(

    "id" UUID NOT NULL,
    "state" TEXT,
    "skipping" BOOLEAN,
    "message_template_fields" JSONB,
    "message_template_type" TEXT,
    "message_type" TEXT,
    "message_address" TEXT,
    "ready_date" TIMESTAMP,
    "last_error_date" TIMESTAMP,
    "send_date" TIMESTAMP,
    "cancel_date" TIMESTAMP,
    "created_date"  TIMESTAMP NOT NULL DEFAULT NOW(),
    "modified_date" TIMESTAMP NOT NULL DEFAULT NOW(),
    "version" BIGINT NOT NULL DEFAULT 1
);
