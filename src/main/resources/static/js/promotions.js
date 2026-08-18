document.addEventListener('DOMContentLoaded', function () {
    console.log('promotions.js chargé correctement');

    var searchInput = document.getElementById('promotion-search');
    var table = document.querySelector('table');

    if (!searchInput) {
        console.warn('Élément #promotion-search introuvable dans le DOM');
    } else if (!table) {
        console.warn('Aucune table trouvée — probablement aucune promotion en base');
    } else {
        searchInput.addEventListener('input', function () {
            var query = searchInput.value.trim().toLowerCase();
            var rows = table.querySelectorAll('tbody tr');

            rows.forEach(function (row) {
                var name = row.getAttribute('data-name');
                if (!name) {
                    console.warn('Ligne sans attribut data-name :', row);
                    return;
                }
                row.style.display = name.toLowerCase().indexOf(query) !== -1 ? '' : 'none';
            });
        });
    }

    var downloadLinks = document.querySelectorAll('.btn-download');
    console.log('Liens de téléchargement trouvés :', downloadLinks.length);

    downloadLinks.forEach(function (link) {
        link.addEventListener('click', function () {
            link.classList.add('loading');
            var originalText = link.textContent;
            link.textContent = 'Génération en cours...';
            setTimeout(function () {
                link.classList.remove('loading');
                link.textContent = originalText;
            }, 2500);
        });
    });
});