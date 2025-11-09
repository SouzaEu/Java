-- Create mood_entries table
CREATE TABLE mood_entries (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    date DATE NOT NULL,
    mood INTEGER NOT NULL CHECK (mood >= 1 AND mood <= 5),
    stress INTEGER NOT NULL CHECK (stress >= 1 AND stress <= 5),
    productivity INTEGER NOT NULL CHECK (productivity >= 1 AND productivity <= 5),
    notes VARCHAR(500),
    tags JSONB,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_user_date UNIQUE(user_id, date)
);

-- Create indexes
CREATE INDEX idx_mood_entries_user_date ON mood_entries(user_id, date DESC);
CREATE INDEX idx_mood_entries_date ON mood_entries(date);
