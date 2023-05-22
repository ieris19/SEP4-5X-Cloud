#Start-up script for the LoraWAN service
echo "Starting LoraWAN service..."

# Create secret properties file from environment variables
echo "Creating secret properties file..."
mkdir config
touch config/secrets.properties
echo "lorawan.url=${lorawan_url}" >> config/secrets.properties
echo "lorawan.EUI_dev=${lorawan_EUI_dev}" >> config/secrets.properties
echo "mongodb.url=${mongodb_url}" >> config/secrets.properties
echo "Secrets file created."

echo "Starting Spring Container..."
java -jar lorawan.jar
echo "Spring Container has terminated"
