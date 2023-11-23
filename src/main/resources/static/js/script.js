function isValidIpAddress(ipAddress) {
    const ipv4Pattern = /^(25[0-5]|2[0-4][0-9]|[0-1]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[0-1]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[0-1]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[0-1]?[0-9][0-9]?)$/;
    return ipv4Pattern.test(ipAddress);
}

new Vue({
    el: '#app',
    data: {
        active: 'home',
        ipAddressesArray: [''],
        result: '',
        ipDataList: [],
        searchResults: []
    },
    methods: {
        makeActive: function(item) {
            this.active = item;
            if (item === 'iptablelist') {
                this.fetchIpData();
            }
        },
        addIpAddress: function() {
            if (this.ipAddressesArray.length < 10) {
                this.ipAddressesArray.push('');
            } else {
                alert('You cannot add more than 10 IP addresses.');
            }
        },
        removeIpAddress: function(index) {
            this.ipAddressesArray.splice(index, 1);
        },
        lookupIp: function() {
            const validIpAddresses = this.ipAddressesArray.filter(ipAddress => isValidIpAddress(ipAddress));
            if (validIpAddresses.length > 0) {
                fetch(`/countriesbatch?ip=${validIpAddresses.join('&ip=')}`)
                    .then(response => response.json())
                    .then(responseData => {
                        // Теперь используем responseData.northCountries вместо data
                        const data = responseData.northCountries;

                        if (!Array.isArray(data)) {
                            console.error('Expected an array from the server, but got:', data);
                            return;
                        }

                        this.searchResults = validIpAddresses.map((ip, index) => {
                            const countryName = data[index] || 'Unknown';
                            const status = countryName === 'Unknown' ? '❌' : '✓';

                            return {
                                ip: ip,
                                countryName: countryName,
                                status: status
                            };
                        });
                    })
                    .catch(error => {
                        console.error('Error:', error);
                    });
            }
        },
        fetchIpData: function() {
            fetch('/ipdata')
                .then(response => response.json())
                .then(data => {
                    this.ipDataList = data;
                })
                .catch(error => {
                    console.error('Error:', error);
                });
        }
    }
});
