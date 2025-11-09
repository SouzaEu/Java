-- Create recommendations table
CREATE TABLE recommendations (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    type VARCHAR(20) NOT NULL,
    message VARCHAR(200) NOT NULL,
    action_url VARCHAR(255),
    scheduled_at TIMESTAMP,
    source VARCHAR(10) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes
CREATE INDEX idx_recommendations_user_created ON recommendations(user_id, created_at DESC);
CREATE INDEX idx_recommendations_user_scheduled ON recommendations(user_id, scheduled_at);
